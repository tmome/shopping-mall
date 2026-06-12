package com.example.shoppingmall.product.service

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import com.example.shoppingmall.common.pagination.PageRequestFactory
import com.example.shoppingmall.product.domain.Product
import com.example.shoppingmall.product.repository.ProductRepository
import com.example.shoppingmall.product.service.model.CreateProductCommand
import com.example.shoppingmall.product.service.model.ProductResult
import com.example.shoppingmall.product.service.model.ProductSortOption
import com.example.shoppingmall.product.service.model.toResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
	private val productRepository: ProductRepository,
) {
	@Transactional(readOnly = true)
	fun findPage(pageable: Pageable, sortOption: ProductSortOption): Page<ProductResult> {
		val pageRequest = PageRequestFactory.of(pageable, sortOption)
		return productRepository.findAll(pageRequest).map { it.toResult() }
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
