package com.example.shoppingmall.order

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant

@RestController
@RequestMapping("/api/orders")
class OrderController(
	private val orderService: OrderService,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@Valid @RequestBody request: CreateOrderRequest): OrderResponse =
		orderService.createOrder(request)

	@GetMapping("/{id}")
	fun get(@PathVariable id: Long): OrderResponse =
		orderService.getOrder(id)
}

data class CreateOrderRequest(
	@field:NotBlank
	val buyerName: String,
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
	val totalAmount: BigDecimal,
	val createdAt: Instant,
	val items: List<OrderItemResponse>,
)

data class OrderItemResponse(
	val productId: Long,
	val productName: String,
	val unitPrice: BigDecimal,
	val quantity: Int,
	val lineAmount: BigDecimal,
)

fun PurchaseOrder.toResponse(): OrderResponse =
	OrderResponse(
		id = requireNotNull(id),
		buyerName = buyerName,
		status = status,
		totalAmount = totalAmount(),
		createdAt = createdAt,
		items = items.map { it.toResponse() },
	)

fun OrderItem.toResponse(): OrderItemResponse =
	OrderItemResponse(
		productId = requireNotNull(product.id),
		productName = productName,
		unitPrice = unitPrice,
		quantity = quantity,
		lineAmount = lineAmount(),
	)
