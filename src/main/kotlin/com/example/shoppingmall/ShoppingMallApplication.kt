package com.example.shoppingmall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ShoppingMallApplication

fun main(args: Array<String>) {
	runApplication<ShoppingMallApplication>(*args)
}
