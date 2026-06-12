package com.example.shoppingmall.product.dto

import com.example.shoppingmall.product.service.model.CreateProductCommand
import com.example.shoppingmall.product.service.model.ProductResult
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateProductRequest(
	@field:NotBlank
	val name: String,

	@field:Min(1)
	val price: Long,

	@field:Min(0)
	val stockQuantity: Int,
)

data class ProductResponse(
	val id: Long,
	val name: String,
	val price: Long,
	val stockQuantity: Int,
)

fun CreateProductRequest.toCommand(): CreateProductCommand {
	return CreateProductCommand(
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
}

fun ProductResult.toResponse(): ProductResponse {
	return ProductResponse(
		id = id,
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
}
