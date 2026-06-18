package com.example.shoppingmall.payment.gateway

import com.example.shoppingmall.payment.domain.PgProvider
import com.example.shoppingmall.payment.service.model.PaymentApproveCommand
import com.example.shoppingmall.payment.service.model.PaymentCancelCommand
import com.example.shoppingmall.payment.service.model.PaymentCancelResult
import com.example.shoppingmall.payment.service.model.PaymentLookupCommand
import com.example.shoppingmall.payment.service.model.PaymentResult

interface PaymentGateway {
	val provider: PgProvider

	fun approve(command: PaymentApproveCommand): PaymentResult

	fun cancel(command: PaymentCancelCommand): PaymentCancelResult

	fun getPayment(command: PaymentLookupCommand): PaymentResult
}
