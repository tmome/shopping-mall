package com.example.shoppingmall.order.service

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import com.example.shoppingmall.member.repository.MemberRepository
import com.example.shoppingmall.order.domain.PurchaseOrder
import com.example.shoppingmall.order.repository.PurchaseOrderRepository
import com.example.shoppingmall.order.service.model.CreateOrderCommand
import com.example.shoppingmall.order.service.model.OrderResult
import com.example.shoppingmall.order.service.model.toResult
import com.example.shoppingmall.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
	private val memberRepository: MemberRepository,
	private val productRepository: ProductRepository,
	private val purchaseOrderRepository: PurchaseOrderRepository,
) {
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

		return purchaseOrderRepository.save(order).toResult()
	}

	@Transactional(readOnly = true)
	fun getOrder(memberId: Long, id: Long): OrderResult {
		return (purchaseOrderRepository.findWithItemsByIdAndMemberId(id, memberId)
			?: throw ApiException(ErrorCode.ORDER_NOT_FOUND))
			.toResult()
	}
}
