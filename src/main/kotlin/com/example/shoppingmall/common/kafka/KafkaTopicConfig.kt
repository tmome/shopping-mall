package com.example.shoppingmall.common.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig {
	@Bean
	@ConditionalOnProperty(
		prefix = "app.kafka",
		name = ["topic-auto-create-enabled"],
		havingValue = "true",
		matchIfMissing = true,
	)
	fun purchaseEventTopic(
		@Value("\${app.kafka.topics.purchase-event}") topicName: String,
	): NewTopic {
		return TopicBuilder.name(topicName)
			.partitions(1)
			.replicas(1)
			.build()
	}

	@Bean
	@ConditionalOnProperty(
		prefix = "app.kafka",
		name = ["topic-auto-create-enabled"],
		havingValue = "true",
		matchIfMissing = true,
	)
	fun purchaseCommandTopic(
		@Value("\${app.kafka.topics.purchase-command}") topicName: String,
	): NewTopic {
		return TopicBuilder.name(topicName)
			.partitions(1)
			.replicas(1)
			.build()
	}
}
