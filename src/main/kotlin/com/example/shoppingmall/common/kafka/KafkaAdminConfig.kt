package com.example.shoppingmall.common.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaAdminConfig(
	private val environment: Environment,
) {
	@Bean
	fun kafkaAdmin(): KafkaAdmin {
		return KafkaAdmin(
			mapOf(
				AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to environment.getProperty(
					"spring.kafka.bootstrap-servers",
					"localhost:9092",
				),
			),
		)
	}
}
