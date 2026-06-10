package com.example.shoppingmall.auth.service

import com.example.shoppingmall.auth.domain.OAuthUserInfoFactory
import com.example.shoppingmall.auth.domain.ShoppingMallPrincipal
import com.example.shoppingmall.member.domain.Member
import com.example.shoppingmall.member.repository.MemberRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
	private val memberRepository: MemberRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private val delegate = DefaultOAuth2UserService()

	@Transactional
	override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
		val oauth2User = delegate.loadUser(userRequest)
		val registrationId = userRequest.clientRegistration.registrationId
		val userInfo = try {
			OAuthUserInfoFactory.from(registrationId, oauth2User.attributes)
		} catch (exception: IllegalArgumentException) {
			throw OAuth2AuthenticationException(exception.message)
		}

		val member = memberRepository.findByProviderAndProviderId(userInfo.provider, userInfo.providerId)
			?.apply { updateProfile(userInfo.email, userInfo.name) }
			?: memberRepository.save(
				Member(
					provider = userInfo.provider,
					providerId = userInfo.providerId,
					email = userInfo.email,
					name = userInfo.name,
				),
			)

		return ShoppingMallPrincipal.from(
			member = member,
			attributes = oauth2User.attributes,
			nameAttributeKey = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName,
		)
	}
}
