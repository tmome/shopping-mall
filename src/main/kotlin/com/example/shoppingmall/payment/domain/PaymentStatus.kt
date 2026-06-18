package com.example.shoppingmall.payment.domain

enum class PaymentStatus {
	READY,
	APPROVED,
	PAID,
	FAILED,
	CANCELLED,
	PARTIAL_CANCELLED,
}
