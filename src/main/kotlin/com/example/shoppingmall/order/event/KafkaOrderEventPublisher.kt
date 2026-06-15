package com.example.shoppingmall.order.event

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.concurrent.CompletableFuture

@Component
class KafkaOrderEventPublisher(
	private val kafkaTemplate: KafkaTemplate<String, Any>,
	@Value("\${app.kafka.topics.purchase-event}") private val purchaseEventTopic: String,
	@Value("\${app.kafka.topics.purchase-command}") private val purchaseCommandTopic: String,
) {
	fun requestOrder(event: OrderRequestedEvent): CompletableFuture<SendResult<String, Any>> {
		return kafkaTemplate.send(purchaseCommandTopic, event.partitionKey(), event)
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	fun publish(event: OrderCreatedEvent) {
		kafkaTemplate.send(purchaseEventTopic, event.orderId.toString(), event)
	}

	fun publish(event: OrderRejectedEvent) {
		kafkaTemplate.send(purchaseEventTopic, event.requestId, event)
	}

	private fun OrderRequestedEvent.partitionKey(): String {
		return items.minOf { it.productId }.toString()
	}
}
