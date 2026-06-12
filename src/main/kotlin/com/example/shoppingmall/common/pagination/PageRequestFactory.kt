package com.example.shoppingmall.common.pagination

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

object PageRequestFactory {
	fun of(pageable: Pageable, sortOption: PageSortOption): PageRequest {
		return PageRequest.of(
			pageable.pageNumber,
			pageable.pageSize,
			sortOption.toSort(),
		)
	}
}
