package com.example.shoppingmall.member.repository

import com.example.shoppingmall.member.domain.Member
import com.example.shoppingmall.member.domain.OAuthProvider
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
	fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): Member?
}
