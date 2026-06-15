package com.example.shoppingmall.common.kafka

import com.example.shoppingmall.order.event.OrderRequestedEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer

@EnableKafka
@Configuration
class KafkaConsumerConfig(
	private val environment: Environment,
) {
	@Bean
	fun consumerFactory(): ConsumerFactory<String, OrderRequestedEvent> {
		val properties = mutableMapOf<String, Any>()
		properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = environment.getProperty(
			"spring.kafka.bootstrap-servers",
			"localhost:9092",
		)
		properties[ConsumerConfig.GROUP_ID_CONFIG] = environment.getProperty(
			"spring.kafka.consumer.group-id",
			"shopping-mall-order-workers",
		)
		properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = environment.getProperty(
			"spring.kafka.consumer.auto-offset-reset",
			"earliest",
		)
		properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
		properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JacksonJsonDeserializer::class.java
		properties[JacksonJsonDeserializer.TRUSTED_PACKAGES] = "com.example.shoppingmall.*"
		properties[JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS] = false
		properties[JacksonJsonDeserializer.VALUE_DEFAULT_TYPE] = OrderRequestedEvent::class.java.name

		return DefaultKafkaConsumerFactory(properties)
	}

	@Bean
	fun kafkaListenerContainerFactory(
		consumerFactory: ConsumerFactory<String, OrderRequestedEvent>,
	): ConcurrentKafkaListenerContainerFactory<String, OrderRequestedEvent> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, OrderRequestedEvent>()
		factory.setConsumerFactory(consumerFactory)
		factory.setConcurrency(environment.getProperty("spring.kafka.listener.concurrency", Int::class.java, 1))
		factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
		factory.setAutoStartup(environment.getProperty("spring.kafka.listener.auto-startup", Boolean::class.java, true))

		return factory
	}
}
