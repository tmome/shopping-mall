package com.example.shoppingmall.order.event

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.order.service.OrderService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumer(
	private val orderService: OrderService,
	private val kafkaOrderEventPublisher: KafkaOrderEventPublisher,
) {
	@KafkaListener(topics = ["\${app.kafka.topics.purchase-command}"])
	fun consume(event: OrderRequestedEvent) {
		try {
			orderService.createOrder(event.memberId, event.toCommand())
		} catch (exception: ApiException) {
			kafkaOrderEventPublisher.publish(OrderRejectedEvent.from(event, exception.message))
		}
	}
}
