package com.example.shoppingmall.common.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable

fun <T> JPAQuery<T>.pageAwareFetch(pageable: Pageable): List<T> {
	return if (pageable.isPaged) {
		offset(pageable.offset).limit(pageable.pageSize.toLong()).fetch()
	} else {
		fetch()
	}
}

fun BooleanExpression?.andIfNotNull(condition: BooleanExpression?): BooleanExpression? {
	return this?.and(condition) ?: condition
}
