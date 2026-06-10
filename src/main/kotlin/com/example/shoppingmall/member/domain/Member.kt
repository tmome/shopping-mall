package com.example.shoppingmall.member.domain

import com.example.shoppingmall.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
	name = "members",
	uniqueConstraints = [
		UniqueConstraint(name = "uk_members_provider_provider_id", columnNames = ["provider", "provider_id"]),
	],
)
class Member(
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	val provider: OAuthProvider,

	@Column(name = "provider_id", nullable = false, length = 100)
	val providerId: String,

	@Column(length = 200)
	var email: String?,

	@Column(nullable = false, length = 80)
	var name: String,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	var role: MemberRole = MemberRole.USER,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
) : BaseEntity() {
	fun updateProfile(email: String?, name: String) {
		this.email = email
		this.name = name
	}
}

enum class OAuthProvider {
	KAKAO,
	NAVER,
}

enum class MemberRole {
	USER,
	ADMIN,
}
