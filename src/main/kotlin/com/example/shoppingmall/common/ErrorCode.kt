package com.example.shoppingmall.common

import org.springframework.http.HttpStatus

enum class ErrorCode(
	val status: HttpStatus,
	val message: String,
) {
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed."),
	INVALID_ORDER_ITEM(HttpStatus.BAD_REQUEST, "Order must contain at least one item."),
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero."),
	NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "Not enough stock."),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
}
