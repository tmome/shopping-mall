package com.example.shoppingmall.common.pagination

import org.springframework.data.domain.Page

data class Pagination(
	val totalCount: Long,
	val totalPage: Int,
	val count: Int,
	val page: Int,
	val perPage: Int,
	val hasNextPage: Boolean,
	val hasPreviousPage: Boolean,
) {
	companion object {
		fun valueOf(page: Page<*>) = Pagination(
			page.totalElements,
			page.totalPages,
			page.numberOfElements,
			page.number + 1,
			page.size,
			page.hasNext(),
			page.hasPrevious(),
		)
	}
}
