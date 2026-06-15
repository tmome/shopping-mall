package com.example.shoppingmall.order.service

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import com.example.shoppingmall.member.repository.MemberRepository
import com.example.shoppingmall.order.domain.PurchaseOrder
import com.example.shoppingmall.order.event.KafkaOrderEventPublisher
import com.example.shoppingmall.order.event.OrderCreatedEvent
import com.example.shoppingmall.order.event.OrderRequestedEvent
import com.example.shoppingmall.order.repository.PurchaseOrderRepository
import com.example.shoppingmall.order.service.model.CreateOrderCommand
import com.example.shoppingmall.order.service.model.OrderRequestResult
import com.example.shoppingmall.order.service.model.OrderResult
import com.example.shoppingmall.order.service.model.toResult
import com.example.shoppingmall.product.repository.ProductRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
	private val memberRepository: MemberRepository,
	private val productRepository: ProductRepository,
	private val purchaseOrderRepository: PurchaseOrderRepository,
	private val applicationEventPublisher: ApplicationEventPublisher,
	private val kafkaOrderEventPublisher: KafkaOrderEventPublisher,
) {
	fun requestOrder(memberId: Long, command: CreateOrderCommand): OrderRequestResult {
		validateOrderRequest(memberId, command)

		val event = OrderRequestedEvent.from(memberId, command)
		kafkaOrderEventPublisher.requestOrder(event).join()

		return OrderRequestResult(
			requestId = event.requestId,
			status = "ACCEPTED",
		)
	}

	@Transactional
	fun createOrder(memberId: Long, command: CreateOrderCommand): OrderResult {
		if (command.items.isEmpty()) {
			throw ApiException(ErrorCode.INVALID_ORDER_ITEM)
		}

		val member = memberRepository.findById(memberId)
			.orElseThrow { ApiException(ErrorCode.MEMBER_NOT_FOUND) }
		val order = PurchaseOrder(
			buyerName = member.name,
			member = member,
		)
		command.items.forEach { item ->
			val product = productRepository.findByIdForUpdate(item.productId)
				?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)

			product.decreaseStock(item.quantity)
			order.addItem(product, item.quantity)
		}

		val result = purchaseOrderRepository.save(order).toResult()
		applicationEventPublisher.publishEvent(OrderCreatedEvent.from(result))

		return result
	}

	@Transactional(readOnly = true)
	fun getOrder(memberId: Long, id: Long): OrderResult {
		return (purchaseOrderRepository.findWithItemsByIdAndMemberId(id, memberId)
			?: throw ApiException(ErrorCode.ORDER_NOT_FOUND))
			.toResult()
	}

	@Transactional(readOnly = true)
	fun validateOrderRequest(memberId: Long, command: CreateOrderCommand) {
		if (command.items.isEmpty()) {
			throw ApiException(ErrorCode.INVALID_ORDER_ITEM)
		}
		if (command.items.any { it.quantity <= 0 }) {
			throw ApiException(ErrorCode.INVALID_QUANTITY)
		}

		if (!memberRepository.existsById(memberId)) {
			throw ApiException(ErrorCode.MEMBER_NOT_FOUND)
		}

		val requestedQuantities = command.items
			.groupingBy { it.productId }
			.fold(0) { total, item -> total + item.quantity }
		val products = productRepository.findAllById(requestedQuantities.keys)
			.associateBy { requireNotNull(it.id) }

		requestedQuantities.forEach { (productId, quantity) ->
			val product = products[productId]
				?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)
			if (product.stockQuantity < quantity) {
				throw ApiException(ErrorCode.NOT_ENOUGH_STOCK)
			}
		}
	}
}
