package com.example.shoppingmall.order

import com.example.shoppingmall.product.Product
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "purchase_orders")
class PurchaseOrder(
	@Column(nullable = false, length = 120)
	val buyerName: String,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	var status: OrderStatus = OrderStatus.CREATED,

	@Column(nullable = false)
	val createdAt: Instant = Instant.now(),

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
) {
	@OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
	val items: MutableList<OrderItem> = mutableListOf()

	fun addItem(product: Product, quantity: Int) {
		items += OrderItem(
			order = this,
			product = product,
			productName = product.name,
			unitPrice = product.price,
			quantity = quantity,
		)
	}

	fun totalAmount(): BigDecimal =
		items.fold(BigDecimal.ZERO) { total, item -> total + item.lineAmount() }
}

@Entity
@Table(name = "order_items")
class OrderItem(
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	val order: PurchaseOrder,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	val product: Product,

	@Column(nullable = false, length = 120)
	val productName: String,

	@Column(nullable = false, precision = 12, scale = 2)
	val unitPrice: BigDecimal,

	@Column(nullable = false)
	val quantity: Int,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
) {
	fun lineAmount(): BigDecimal = unitPrice * quantity.toBigDecimal()
}
