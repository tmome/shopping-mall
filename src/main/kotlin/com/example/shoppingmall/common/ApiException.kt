package com.example.shoppingmall.common

class ApiException(
	val errorCode: ErrorCode,
	override val message: String = errorCode.message,
) : RuntimeException(message)
