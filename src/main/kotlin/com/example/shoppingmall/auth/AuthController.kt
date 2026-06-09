package com.example.shoppingmall.auth

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
	@GetMapping("/me")
	fun me(@AuthenticationPrincipal principal: ShoppingMallPrincipal?): AuthMeResponse =
		AuthMeResponse(
			authenticated = principal != null,
			memberId = principal?.memberId,
			email = principal?.email,
			name = principal?.displayName,
		)
}

data class AuthMeResponse(
	val authenticated: Boolean,
	val memberId: Long?,
	val email: String?,
	val name: String?,
)
