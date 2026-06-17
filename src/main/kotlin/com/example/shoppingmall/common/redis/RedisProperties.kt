package com.example.shoppingmall.common.redis

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnProperty(prefix = "app.redis", name = ["host"])
@ConfigurationProperties(prefix = "app.redis")
data class RedisProperties(
	val envPrefix: String,
	val database: Int,
	val host: String,
	val port: Int,
	val batchSize: Int,
)
