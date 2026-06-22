package com.example.shoppingmall.auth.verification.gateway

import com.example.shoppingmall.auth.verification.service.model.VerificationConfirmCommand
import com.example.shoppingmall.auth.verification.service.model.VerificationConfirmResult
import com.example.shoppingmall.auth.verification.service.model.VerificationIssueCommand
import com.example.shoppingmall.auth.verification.service.model.VerificationIssueResult

interface SignupVerificationGateway {
	fun issue(command: VerificationIssueCommand): VerificationIssueResult

	fun confirm(command: VerificationConfirmCommand): VerificationConfirmResult
}
