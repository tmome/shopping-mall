package com.example.shoppingmall.product.controller

import com.example.shoppingmall.product.dto.CreateProductRequest
import com.example.shoppingmall.product.dto.ProductResponse
import com.example.shoppingmall.product.dto.toCommand
import com.example.shoppingmall.product.dto.toResponse
import com.example.shoppingmall.product.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
	private val productService: ProductService,
) {
	@GetMapping
	fun list(): List<ProductResponse> {
		return productService.findAll().map { it.toResponse() }
	}

	@GetMapping("/{id}")
	fun get(@PathVariable id: Long): ProductResponse {
		return productService.findById(id).toResponse()
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@Valid @RequestBody request: CreateProductRequest): ProductResponse {
		return productService.create(request.toCommand()).toResponse()
	}
}
