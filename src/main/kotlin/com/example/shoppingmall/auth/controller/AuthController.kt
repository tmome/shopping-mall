package com.example.shoppingmall.auth.controller

import com.example.shoppingmall.auth.domain.ShoppingMallPrincipal
import com.example.shoppingmall.auth.dto.AuthMeResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
	@GetMapping("/me")
	fun me(@AuthenticationPrincipal principal: ShoppingMallPrincipal?): AuthMeResponse {
		return AuthMeResponse(
			authenticated = principal != null,
			memberId = principal?.memberId,
			email = principal?.email,
			name = principal?.displayName,
		)
	}
}
