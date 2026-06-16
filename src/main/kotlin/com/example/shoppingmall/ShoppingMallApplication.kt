package com.example.shoppingmall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackages = [
		"com.example.shoppingmall.member.repository",
		"com.example.shoppingmall.order.repository",
		"com.example.shoppingmall.product.repository",
	],
)
@EnableElasticsearchRepositories(basePackages = ["com.example.shoppingmall.product.search"])
class ShoppingMallApplication

fun main(args: Array<String>) {
	runApplication<ShoppingMallApplication>(*args)
}
