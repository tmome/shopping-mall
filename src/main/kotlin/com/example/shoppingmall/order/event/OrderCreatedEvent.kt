package com.example.shoppingmall.order.event

import com.example.shoppingmall.order.domain.OrderStatus
import com.example.shoppingmall.order.service.model.CreateOrderCommand
import com.example.shoppingmall.order.service.model.CreateOrderItemCommand
import com.example.shoppingmall.order.service.model.OrderItemResult
import com.example.shoppingmall.order.service.model.OrderResult
import java.time.Instant
import java.util.UUID

data class OrderRequestedEvent(
	val requestId: String,
	val memberId: Long,
	val requestedAt: Instant,
	val items: List<OrderRequestedItemEvent>,
) {
	companion object {
		fun from(memberId: Long, command: CreateOrderCommand): OrderRequestedEvent {
			return OrderRequestedEvent(
				requestId = UUID.randomUUID().toString(),
				memberId = memberId,
				requestedAt = Instant.now(),
				items = command.items.map { OrderRequestedItemEvent.from(it) },
			)
		}
	}

	fun toCommand(): CreateOrderCommand {
		return CreateOrderCommand(
			items = items.map { it.toCommand() },
		)
	}
}

data class OrderRequestedItemEvent(
	val productId: Long,
	val quantity: Int,
) {
	companion object {
		fun from(command: CreateOrderItemCommand): OrderRequestedItemEvent {
			return OrderRequestedItemEvent(
				productId = command.productId,
				quantity = command.quantity,
			)
		}
	}

	fun toCommand(): CreateOrderItemCommand {
		return CreateOrderItemCommand(
			productId = productId,
			quantity = quantity,
		)
	}
}

data class OrderRejectedEvent(
	val eventType: String = "PURCHASE_REJECTED",
	val requestId: String,
	val memberId: Long,
	val reason: String,
	val rejectedAt: Instant,
) {
	companion object {
		fun from(event: OrderRequestedEvent, reason: String): OrderRejectedEvent {
			return OrderRejectedEvent(
				requestId = event.requestId,
				memberId = event.memberId,
				reason = reason,
				rejectedAt = Instant.now(),
			)
		}
	}
}

data class OrderCreatedEvent(
	val eventType: String = "PURCHASE_COMPLETED",
	val orderId: Long,
	val buyerName: String,
	val status: OrderStatus,
	val totalAmount: Long,
	val createdAt: Instant,
	val items: List<OrderCreatedItemEvent>,
) {
	companion object {
		fun from(result: OrderResult): OrderCreatedEvent {
			return OrderCreatedEvent(
				orderId = result.id,
				buyerName = result.buyerName,
				status = result.status,
				totalAmount = result.totalAmount,
				createdAt = result.createdAt,
				items = result.items.map { OrderCreatedItemEvent.from(it) },
			)
		}
	}
}

data class OrderCreatedItemEvent(
	val productId: Long,
	val productName: String,
	val unitPrice: Long,
	val quantity: Int,
	val lineAmount: Long,
) {
	companion object {
		fun from(result: OrderItemResult): OrderCreatedItemEvent {
			return OrderCreatedItemEvent(
				productId = result.productId,
				productName = result.productName,
				unitPrice = result.unitPrice,
				quantity = result.quantity,
				lineAmount = result.lineAmount,
			)
		}
	}
}
