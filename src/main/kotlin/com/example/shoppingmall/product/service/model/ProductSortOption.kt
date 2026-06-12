package com.example.shoppingmall.product.service.model

import com.example.shoppingmall.common.pagination.PageSortOption
import org.springframework.data.domain.Sort

enum class ProductSortOption(
	private val sort: Sort,
) : PageSortOption {
	LATEST(Sort.by(Sort.Order.desc("id"))),
	RECOMMENDED(Sort.by(Sort.Order.desc("stockQuantity"), Sort.Order.desc("id"))),
	POPULAR(Sort.by(Sort.Order.desc("price"), Sort.Order.desc("id")));

	override fun toSort(): Sort {
		return sort
	}

	companion object {
		fun from(value: String): ProductSortOption {
			return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
				?: LATEST
		}
	}
}
