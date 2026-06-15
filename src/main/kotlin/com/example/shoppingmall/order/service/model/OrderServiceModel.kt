package com.example.shoppingmall.order.service.model

import com.example.shoppingmall.order.domain.OrderItem
import com.example.shoppingmall.order.domain.OrderStatus
import com.example.shoppingmall.order.domain.PurchaseOrder
import java.time.Instant

data class CreateOrderCommand(
	val items: List<CreateOrderItemCommand>,
)

data class CreateOrderItemCommand(
	val productId: Long,
	val quantity: Int,
)

data class OrderRequestResult(
	val requestId: String,
	val status: String,
)

data class OrderResult(
	val id: Long,
	val buyerName: String,
	val status: OrderStatus,
	val totalAmount: Long,
	val createdAt: Instant,
	val items: List<OrderItemResult>,
)

data class OrderItemResult(
	val productId: Long,
	val productName: String,
	val unitPrice: Long,
	val quantity: Int,
	val lineAmount: Long,
)

fun PurchaseOrder.toResult(): OrderResult {
	return OrderResult(
		id = requireNotNull(id),
		buyerName = buyerName,
		status = status,
		totalAmount = totalAmount(),
		createdAt = requireNotNull(createdAt),
		items = items.map { it.toResult() },
	)
}

fun OrderItem.toResult(): OrderItemResult {
	return OrderItemResult(
		productId = requireNotNull(product.id),
		productName = productName,
		unitPrice = unitPrice,
		quantity = quantity,
		lineAmount = lineAmount(),
	)
}
