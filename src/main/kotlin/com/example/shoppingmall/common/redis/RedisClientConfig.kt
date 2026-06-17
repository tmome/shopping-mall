package com.example.shoppingmall.common.redis

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@EnableConfigurationProperties(RedisProperties::class)
@ConditionalOnProperty(prefix = "app.redis", name = ["host"])
@Configuration
class RedisClientConfig(private val redisProperties: RedisProperties) {
	@Bean
	fun redisConnectionFactory(): LettuceConnectionFactory {
		val redisConfiguration = RedisStandaloneConfiguration()
		redisConfiguration.database = redisProperties.database
		redisConfiguration.hostName = redisProperties.host
		redisConfiguration.port = redisProperties.port
		return LettuceConnectionFactory(redisConfiguration)
	}

	@Primary
	@Bean
	fun redisTemplate(): RedisTemplate<String, String> {
		val redisTemplate = RedisTemplate<String, String>()
		redisTemplate.setConnectionFactory(redisConnectionFactory())
		redisTemplate.keySerializer = StringRedisSerializer()
		redisTemplate.valueSerializer = StringRedisSerializer()
		return redisTemplate
	}
}
