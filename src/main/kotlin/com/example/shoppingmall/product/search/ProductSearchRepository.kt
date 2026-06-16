package com.example.shoppingmall.product.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ProductSearchRepository : ElasticsearchRepository<ProductSearchDocument, Long> {
	@Query("""{"match":{"name":{"query":"?0"}}}""")
	fun searchByName(keyword: String, pageable: Pageable): Page<ProductSearchDocument>
}
