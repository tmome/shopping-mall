package com.example.shoppingmall.product

import com.example.shoppingmall.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "products")
class Product(
	@Column(nullable = false, length = 120)
	var name: String,

	@Column(nullable = false, precision = 12, scale = 2)
	var price: BigDecimal,

	@Column(nullable = false)
	var stockQuantity: Int,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
) : BaseEntity() {
	fun decreaseStock(quantity: Int) {
		require(quantity > 0) { "Quantity must be greater than zero." }
		require(stockQuantity >= quantity) { "Not enough stock." }
		stockQuantity -= quantity
	}
}
