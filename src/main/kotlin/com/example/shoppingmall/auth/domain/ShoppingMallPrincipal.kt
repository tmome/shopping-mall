package com.example.shoppingmall.auth.domain

import com.example.shoppingmall.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class ShoppingMallPrincipal(
	val memberId: Long,
	val email: String?,
	val displayName: String,
	private val role: String,
	private val delegateAttributes: Map<String, Any>,
	private val nameAttributeKey: String,
) : OAuth2User {
	override fun getName(): String {
		return memberId.toString()
	}

	override fun getAttributes(): Map<String, Any> {
		return delegateAttributes
	}

	override fun getAuthorities(): Collection<GrantedAuthority> {
		return listOf(SimpleGrantedAuthority("ROLE_$role"))
	}

	companion object {
		fun from(member: Member, attributes: Map<String, Any>, nameAttributeKey: String): ShoppingMallPrincipal {
			return ShoppingMallPrincipal(
				memberId = requireNotNull(member.id),
				email = member.email,
				displayName = member.name,
				role = member.role.name,
				delegateAttributes = attributes,
				nameAttributeKey = nameAttributeKey,
			)
		}
	}
}
