package com.example.shoppingmall.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
	@ExceptionHandler(NoSuchElementException::class)
	fun handleNotFound(exception: NoSuchElementException): ResponseEntity<ApiErrorResponse> =
		ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ApiErrorResponse(message = exception.message ?: "Resource not found."))

	@ExceptionHandler(IllegalArgumentException::class)
	fun handleBadRequest(exception: IllegalArgumentException): ResponseEntity<ApiErrorResponse> =
		ResponseEntity.badRequest()
			.body(ApiErrorResponse(message = exception.message ?: "Invalid request."))

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidation(exception: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
		val message = exception.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
			?: "Validation failed."
		return ResponseEntity.badRequest().body(ApiErrorResponse(message = message))
	}
}

data class ApiErrorResponse(
	val message: String,
)
