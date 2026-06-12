package com.example.shoppingmall.common.pagination

import org.springframework.data.domain.Sort

interface PageSortOption {
	fun toSort(): Sort
}
