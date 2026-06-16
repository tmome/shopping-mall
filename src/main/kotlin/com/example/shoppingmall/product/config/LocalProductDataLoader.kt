package com.example.shoppingmall.product.config

import com.example.shoppingmall.product.domain.Product
import com.example.shoppingmall.product.repository.ProductRepository
import com.example.shoppingmall.product.search.ProductSearchService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile("local")
class LocalProductDataLoader(
	private val productRepository: ProductRepository,
	private val productSearchService: ProductSearchService,
) : ApplicationRunner {
	@Transactional
	override fun run(args: ApplicationArguments) {
		val products = listOf(
			Product(name = "더스트 프리 벤토나이트 오리지널", price = 23900, stockQuantity = 120),
			Product(name = "힐링 슬리커 브러쉬", price = 20900, stockQuantity = 80),
			Product(name = "스마일 패드 대형", price = 11900, stockQuantity = 150),
			Product(name = "이지 페이셜 콤", price = 6900, stockQuantity = 90),
			Product(name = "퓨어 튜나 스틱", price = 4500, stockQuantity = 240),
			Product(name = "덴탈 클린 칫솔", price = 7900, stockQuantity = 110),
			Product(name = "릴렉스 샤워기", price = 34900, stockQuantity = 45),
			Product(name = "극세사 펫 타월", price = 15900, stockQuantity = 70),
			Product(name = "버그아웃 라이트 클립", price = 14900, stockQuantity = 65),
			Product(name = "헬스 체크 컬러 카사바", price = 75000, stockQuantity = 30),
			Product(name = "해피 맥스 캣 스프레이", price = 19900, stockQuantity = 55),
			Product(name = "허니 터키츄", price = 3400, stockQuantity = 300),
		)

		products
			.filterNot { productRepository.existsByName(it.name) }
			.forEach { productRepository.save(it) }

		productSearchService.indexAll(productRepository.findAll())
	}
}
