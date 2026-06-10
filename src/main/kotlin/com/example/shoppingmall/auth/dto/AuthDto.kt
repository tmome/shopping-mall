package com.example.shoppingmall.auth.dto

data class AuthMeResponse(
	val authenticated: Boolean,
	val memberId: Long?,
	val email: String?,
	val name: String?,
)
