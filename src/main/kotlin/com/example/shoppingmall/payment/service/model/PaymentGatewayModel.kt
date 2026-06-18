package com.example.shoppingmall.payment.service.model

import com.example.shoppingmall.payment.domain.PaymentMethod
import com.example.shoppingmall.payment.domain.PaymentStatus
import com.example.shoppingmall.payment.domain.PgProvider
import java.time.Instant

data class PaymentApproveCommand(
	val orderId: Long,
	val orderName: String,
	val paymentKey: String,
	val amount: Long,
	val currency: String = "KRW",
	val metadata: Map<String, String> = emptyMap(),
)

data class PaymentCancelCommand(
	val paymentKey: String,
	val reason: String,
	val amount: Long? = null,
	val metadata: Map<String, String> = emptyMap(),
)

data class PaymentLookupCommand(
	val paymentKey: String,
)

data class PaymentResult(
	val provider: PgProvider,
	val paymentKey: String,
	val orderId: Long?,
	val status: PaymentStatus,
	val method: PaymentMethod,
	val totalAmount: Long,
	val approvedAmount: Long,
	val cancelledAmount: Long,
	val currency: String,
	val approvedAt: Instant?,
	val metadata: Map<String, String> = emptyMap(),
)

data class PaymentCancelResult(
	val provider: PgProvider,
	val paymentKey: String,
	val status: PaymentStatus,
	val cancelledAmount: Long,
	val remainingAmount: Long,
	val cancelledAt: Instant?,
	val metadata: Map<String, String> = emptyMap(),
)
