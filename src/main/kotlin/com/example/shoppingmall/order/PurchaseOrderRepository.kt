package com.example.shoppingmall.order

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository : JpaRepository<PurchaseOrder, Long> {
	@EntityGraph(attributePaths = ["items", "items.product"])
	fun findWithItemsById(id: Long): PurchaseOrder?
}
