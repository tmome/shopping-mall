package com.example.shoppingmall.product

import com.example.shoppingmall.common.ApiException
import com.example.shoppingmall.common.ErrorCode
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
	private val productRepository: ProductRepository,
) {
	@GetMapping
	fun list(): List<ProductResponse> =
		productRepository.findAll().map { it.toResponse() }

	@GetMapping("/{id}")
	fun get(@PathVariable id: Long): ProductResponse =
		productRepository.findById(id)
			.orElseThrow { ApiException(ErrorCode.PRODUCT_NOT_FOUND) }
			.toResponse()

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@Valid @RequestBody request: CreateProductRequest): ProductResponse {
		val product = Product(
			name = request.name,
			price = request.price,
			stockQuantity = request.stockQuantity,
		)
		return productRepository.save(product).toResponse()
	}
}

data class CreateProductRequest(
	@field:NotBlank
	val name: String,

	@field:Min(1)
	val price: Long,

	@field:Min(0)
	val stockQuantity: Int,
)

data class ProductResponse(
	val id: Long,
	val name: String,
	val price: Long,
	val stockQuantity: Int,
)

fun Product.toResponse(): ProductResponse =
	ProductResponse(
		id = requireNotNull(id),
		name = name,
		price = price,
		stockQuantity = stockQuantity,
	)
