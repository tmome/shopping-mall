package com.example.shoppingmall.order.repository

import com.example.shoppingmall.order.domain.PurchaseOrder
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository : JpaRepository<PurchaseOrder, Long> {
	@EntityGraph(attributePaths = ["items", "items.product"])
	fun findWithItemsByIdAndMemberId(id: Long, memberId: Long): PurchaseOrder?
}
