package com.example.shoppingmall.product.service.model

import com.example.shoppingmall.product.domain.Product

data class CreateProductCommand(
	val name: String,
	val price: Long,
	val stockQuantity: Int,
)

data class ProductResult(
	val id: Long,
	val name: String,
	val price: Long,
	val stockQuantity: Int,
)

fun Product.toResult(): ProductResult {
	return ProductResult(
		id = requireNotNull(id),
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
}
