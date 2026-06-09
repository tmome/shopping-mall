package com.example.shoppingmall.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
	fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): Member?
}
