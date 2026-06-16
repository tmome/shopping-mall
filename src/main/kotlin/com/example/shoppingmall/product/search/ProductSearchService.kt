package com.example.shoppingmall.product.search

import com.example.shoppingmall.common.pagination.PageRequestFactory
import com.example.shoppingmall.product.domain.Product
import com.example.shoppingmall.product.service.model.ProductResult
import com.example.shoppingmall.product.service.model.ProductSortOption
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Service

@Service
class ProductSearchService(
	private val productSearchRepository: ProductSearchRepository,
	private val elasticsearchOperations: ElasticsearchOperations,
) {
	private val log = LoggerFactory.getLogger(javaClass)

	fun search(keyword: String, pageable: Pageable, sortOption: ProductSortOption): Page<ProductResult> {
		ensureIndexExists()

		val pageRequest = PageRequestFactory.of(pageable, sortOption)
		return productSearchRepository.searchByName(keyword.trim(), pageRequest)
			.map { it.toResult() }
	}

	fun index(product: Product) {
		runCatching {
			ensureIndexExists()
			productSearchRepository.save(product.toSearchDocument())
		}.onFailure {
			log.warn("Failed to index product. productId={}", product.id, it)
		}
	}

	fun indexAll(products: Collection<Product>) {
		if (products.isEmpty()) {
			return
		}

		runCatching {
			ensureIndexExists()
			productSearchRepository.saveAll(products.map { it.toSearchDocument() })
		}.onFailure {
			log.warn("Failed to index products. count={}", products.size, it)
		}
	}

	private fun ensureIndexExists() {
		val indexOperations = elasticsearchOperations.indexOps(ProductSearchDocument::class.java)
		if (!indexOperations.exists()) {
			indexOperations.createWithMapping()
		}
	}
}
