package com.example.shoppingmall.common.pagination

import org.springframework.data.domain.Page

data class PaginationImpl<T>(
	val pagination: Pagination,
	val content: List<T>,
) {
	companion object
}

fun <T : Any, R> Page<T>.toPaginationImpl(mapper: (T) -> R): PaginationImpl<R> {
	return PaginationImpl(
		pagination = Pagination.valueOf(this),
		content = content.map(mapper),
	)
}
