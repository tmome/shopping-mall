package com.example.shoppingmall.order.controller

import com.example.shoppingmall.auth.domain.ShoppingMallPrincipal
import com.example.shoppingmall.order.dto.CreateOrderRequest
import com.example.shoppingmall.order.dto.OrderRequestResponse
import com.example.shoppingmall.order.dto.OrderResponse
import com.example.shoppingmall.order.dto.toCommand
import com.example.shoppingmall.order.dto.toResponse
import com.example.shoppingmall.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
	private val orderService: OrderService,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@AuthenticationPrincipal principal: ShoppingMallPrincipal,
		@Valid @RequestBody request: CreateOrderRequest,
	): OrderResponse {
		return orderService.createOrder(principal.memberId, request.toCommand()).toResponse()
	}

	@PostMapping("/requests")
	@ResponseStatus(HttpStatus.ACCEPTED)
	fun request(
		@AuthenticationPrincipal principal: ShoppingMallPrincipal,
		@Valid @RequestBody request: CreateOrderRequest,
	): OrderRequestResponse {
		return orderService.requestOrder(principal.memberId, request.toCommand()).toResponse()
	}

	@GetMapping("/{id}")
	fun get(
		@AuthenticationPrincipal principal: ShoppingMallPrincipal,
		@PathVariable id: Long,
	): OrderResponse {
		return orderService.getOrder(principal.memberId, id).toResponse()
	}
}
