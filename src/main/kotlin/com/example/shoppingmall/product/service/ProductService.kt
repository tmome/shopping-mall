package com.example.shoppingmall.product.service

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import com.example.shoppingmall.product.domain.Product
import com.example.shoppingmall.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
	private val productRepository: ProductRepository,
) {
	@Transactional(readOnly = true)
	fun findAll(): List<ProductResult> {
		return productRepository.findAll().map { it.toResult() }
	}

	@Transactional(readOnly = true)
	fun findById(id: Long): ProductResult {
		return productRepository.findById(id)
			.orElseThrow { ApiException(ErrorCode.PRODUCT_NOT_FOUND) }
			.toResult()
	}

	@Transactional
	fun create(command: CreateProductCommand): ProductResult {
		val product = Product(
			name = command.name,
			price = command.price,
			stockQuantity = command.stockQuantity,
		)
		return productRepository.save(product).toResult()
	}
}
