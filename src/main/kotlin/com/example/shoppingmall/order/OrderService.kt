package com.example.shoppingmall.order

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import com.example.shoppingmall.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
	private val productRepository: ProductRepository,
	private val purchaseOrderRepository: PurchaseOrderRepository,
) {
	@Transactional
	fun createOrder(request: CreateOrderRequest): OrderResponse {
		if (request.items.isEmpty()) {
			throw ApiException(ErrorCode.INVALID_ORDER_ITEM)
		}

		val order = PurchaseOrder(buyerName = request.buyerName)
		request.items.forEach { item ->
			val product = productRepository.findByIdForUpdate(item.productId)
				?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)

			product.decreaseStock(item.quantity)
			order.addItem(product, item.quantity)
		}

		return purchaseOrderRepository.save(order).toResponse()
	}

	@Transactional(readOnly = true)
	fun getOrder(id: Long): OrderResponse =
		(purchaseOrderRepository.findWithItemsById(id)
			?: throw ApiException(ErrorCode.ORDER_NOT_FOUND))
			.toResponse()
}
