package com.example.shoppingmall.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ApiExceptionHandler {
	@ExceptionHandler(ApiException::class)
	fun handleApiException(exception: ApiException): ResponseEntity<ApiErrorResponse> =
		errorResponse(exception.errorCode, exception.message)

	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	fun handleTypeMismatch(exception: MethodArgumentTypeMismatchException): ResponseEntity<ApiErrorResponse> =
		errorResponse(ErrorCode.VALIDATION_FAILED)

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidation(exception: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
		val message = exception.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
			?: ErrorCode.VALIDATION_FAILED.message
		return errorResponse(ErrorCode.VALIDATION_FAILED, message)
	}

	@ExceptionHandler(Exception::class)
	fun handleUnexpected(exception: Exception): ResponseEntity<ApiErrorResponse> =
		errorResponse(ErrorCode.INTERNAL_SERVER_ERROR)

	private fun errorResponse(
		errorCode: ErrorCode,
		message: String = errorCode.message,
	): ResponseEntity<ApiErrorResponse> =
		ResponseEntity.status(errorCode.status)
			.body(ApiErrorResponse(code = errorCode.name, message = message))
}

data class ApiErrorResponse(
	val code: String,
	val message: String,
)
