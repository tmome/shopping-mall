package com.example.shoppingmall.order.dto

import com.example.shoppingmall.order.domain.OrderStatus
import com.example.shoppingmall.order.service.model.CreateOrderCommand
import com.example.shoppingmall.order.service.model.CreateOrderItemCommand
import com.example.shoppingmall.order.service.model.OrderItemResult
import com.example.shoppingmall.order.service.model.OrderRequestResult
import com.example.shoppingmall.order.service.model.OrderResult
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import java.time.Instant

data class CreateOrderRequest(
	@field:Valid
	val items: List<CreateOrderItemRequest>,
)

data class CreateOrderItemRequest(
	@field:Min(1)
	val productId: Long,
	@field:Min(1)
	val quantity: Int,
)

data class OrderResponse(
	val id: Long,
	val buyerName: String,
	val status: OrderStatus,
	val totalAmount: Long,
	val createdAt: Instant,
	val items: List<OrderItemResponse>,
)

data class OrderItemResponse(
	val productId: Long,
	val productName: String,
	val unitPrice: Long,
	val quantity: Int,
	val lineAmount: Long,
)

data class OrderRequestResponse(
	val requestId: String,
	val status: String,
)

fun CreateOrderRequest.toCommand(): CreateOrderCommand {
	return CreateOrderCommand(
		items = items.map { it.toCommand() },
	)
}

fun CreateOrderItemRequest.toCommand(): CreateOrderItemCommand {
	return CreateOrderItemCommand(
		productId = productId,
		quantity = quantity,
	)
}

fun OrderResult.toResponse(): OrderResponse {
	return OrderResponse(
		id = id,
		buyerName = buyerName,
		status = status,
		totalAmount = totalAmount,
		createdAt = createdAt,
		items = items.map { it.toResponse() },
	)
}

fun OrderItemResult.toResponse(): OrderItemResponse {
	return OrderItemResponse(
		productId = productId,
		productName = productName,
		unitPrice = unitPrice,
		quantity = quantity,
		lineAmount = lineAmount,
	)
}

fun OrderRequestResult.toResponse(): OrderRequestResponse {
	return OrderRequestResponse(
		requestId = requestId,
		status = status,
	)
}
