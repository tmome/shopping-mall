package com.example.shoppingmall.order

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
		require(request.items.isNotEmpty()) { "Order must contain at least one item." }

		val order = PurchaseOrder(buyerName = request.buyerName)
		request.items.forEach { item ->
			val product = productRepository.findByIdForUpdate(item.productId)
				?: throw NoSuchElementException("Product not found: ${item.productId}")

			product.decreaseStock(item.quantity)
			order.addItem(product, item.quantity)
		}

		return purchaseOrderRepository.save(order).toResponse()
	}

	@Transactional(readOnly = true)
	fun getOrder(id: Long): OrderResponse =
		(purchaseOrderRepository.findWithItemsById(id)
			?: throw NoSuchElementException("Order not found: $id"))
			.toResponse()
}
