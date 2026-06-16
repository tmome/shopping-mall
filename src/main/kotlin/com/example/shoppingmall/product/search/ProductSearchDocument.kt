package com.example.shoppingmall.product.search

import com.example.shoppingmall.product.domain.Product
import com.example.shoppingmall.product.service.model.ProductResult
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting

@Document(indexName = "products", createIndex = false)
@Setting(settingPath = "elasticsearch/product-settings.json")
data class ProductSearchDocument(
	@Id
	@Field(type = FieldType.Long)
	val id: Long,

	@Field(
		type = FieldType.Text,
		analyzer = "product_name_index_analyzer",
		searchAnalyzer = "product_name_search_analyzer",
	)
	val name: String,

	@Field(type = FieldType.Long)
	val price: Long,

	@Field(type = FieldType.Integer)
	val stockQuantity: Int,
)

fun Product.toSearchDocument(): ProductSearchDocument {
	return ProductSearchDocument(
		id = requireNotNull(id),
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
}

fun ProductSearchDocument.toResult(): ProductResult {
	return ProductResult(
		id = id,
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
}
