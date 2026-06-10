package com.example.shoppingmall.product.domain

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.BaseEntity
import com.example.shoppingmall.common.ErrorCode
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
	@Column(nullable = false, length = 120)
	var name: String,

	@Column(nullable = false)
	var price: Long,

	@Column(nullable = false)
	var stockQuantity: Int,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
) : BaseEntity() {
	fun decreaseStock(quantity: Int) {
		if (quantity <= 0) {
			throw ApiException(ErrorCode.INVALID_QUANTITY)
		}
		if (stockQuantity < quantity) {
			throw ApiException(ErrorCode.NOT_ENOUGH_STOCK)
		}
		stockQuantity -= quantity
	}
}
