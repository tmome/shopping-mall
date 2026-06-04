package com.example.shoppingmall.common

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
	info = Info(
		title = "Shopping Mall API",
		version = "v1",
		description = "하고 싶은대로 개발해보는 쇼핑몰 API",
	),
)
class OpenApiConfig
