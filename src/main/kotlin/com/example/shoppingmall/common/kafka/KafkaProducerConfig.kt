package com.example.shoppingmall.common.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JacksonJsonSerializer

@Configuration
class KafkaProducerConfig(
	private val environment: Environment,
) {
	@Bean
	fun producerFactory(): ProducerFactory<String, Any> {
		val properties = mutableMapOf<String, Any>()
		properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = environment.getProperty(
			"spring.kafka.bootstrap-servers",
			"localhost:9092",
		)
		properties[ProducerConfig.ACKS_CONFIG] = "all"
		properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
		properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JacksonJsonSerializer::class.java
		properties[JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS] = false

		return DefaultKafkaProducerFactory(properties)
	}

	@Bean
	fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>): KafkaTemplate<String, Any> {
		return KafkaTemplate(producerFactory)
	}
}
