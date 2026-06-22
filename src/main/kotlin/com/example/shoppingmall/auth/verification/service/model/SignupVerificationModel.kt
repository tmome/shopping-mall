package com.example.shoppingmall.auth.verification.service.model

import com.example.shoppingmall.auth.verification.domain.VerificationChannel
import com.example.shoppingmall.auth.verification.domain.VerificationPurpose
import com.example.shoppingmall.auth.verification.domain.VerificationStatus
import java.time.Instant

data class VerificationIssueCommand(
	val channel: VerificationChannel,
	val recipient: String,
	val purpose: VerificationPurpose = VerificationPurpose.SIGN_UP,
	val metadata: Map<String, String> = emptyMap(),
)

data class VerificationIssueResult(
	val verificationId: String,
	val channel: VerificationChannel,
	val recipient: String,
	val purpose: VerificationPurpose,
	val status: VerificationStatus,
	val expiresAt: Instant,
)

data class VerificationConfirmCommand(
	val verificationId: String,
	val recipient: String,
	val code: String,
	val purpose: VerificationPurpose = VerificationPurpose.SIGN_UP,
)

data class VerificationConfirmResult(
	val verificationId: String,
	val recipient: String,
	val purpose: VerificationPurpose,
	val status: VerificationStatus,
	val verifiedAt: Instant?,
)
