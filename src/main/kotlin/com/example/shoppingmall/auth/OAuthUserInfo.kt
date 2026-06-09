package com.example.shoppingmall.auth

import com.example.shoppingmall.member.OAuthProvider

data class OAuthUserInfo(
	val provider: OAuthProvider,
	val providerId: String,
	val email: String?,
	val name: String,
)

object OAuthUserInfoFactory {
	fun from(registrationId: String, attributes: Map<String, Any>): OAuthUserInfo =
		when (registrationId.lowercase()) {
			"kakao" -> kakao(attributes)
			"naver" -> naver(attributes)
			else -> throw IllegalArgumentException("Unsupported OAuth provider: $registrationId")
		}

	@Suppress("UNCHECKED_CAST")
	private fun kakao(attributes: Map<String, Any>): OAuthUserInfo {
		val account = attributes["kakao_account"] as? Map<String, Any> ?: emptyMap()
		val properties = attributes["properties"] as? Map<String, Any> ?: emptyMap()
		val providerId = attributes["id"]?.toString()
			?: throw IllegalArgumentException("Kakao response does not contain id.")
		val email = account["email"] as? String
		val name = (account["name"] as? String)
			?: (properties["nickname"] as? String)
			?: email
			?: "Kakao User"

		return OAuthUserInfo(
			provider = OAuthProvider.KAKAO,
			providerId = providerId,
			email = email,
			name = name,
		)
	}

	@Suppress("UNCHECKED_CAST")
	private fun naver(attributes: Map<String, Any>): OAuthUserInfo {
		val response = attributes["response"] as? Map<String, Any>
			?: throw IllegalArgumentException("Naver response does not contain response.")
		val providerId = response["id"]?.toString()
			?: throw IllegalArgumentException("Naver response does not contain id.")
		val email = response["email"] as? String
		val name = (response["name"] as? String)
			?: email
			?: "Naver User"

		return OAuthUserInfo(
			provider = OAuthProvider.NAVER,
			providerId = providerId,
			email = email,
			name = name,
		)
	}
}
