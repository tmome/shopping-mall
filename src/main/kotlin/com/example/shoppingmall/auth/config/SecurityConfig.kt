package com.example.shoppingmall.auth.config

import com.example.shoppingmall.auth.service.CustomOAuth2UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
	private val customOAuth2UserService: CustomOAuth2UserService,
) {
	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		return http
			.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
			.authorizeHttpRequests {
				it.requestMatchers("/", "/index.html", "/app.js", "/styles.css", "/favicon.ico").permitAll()
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.requestMatchers("/oauth2/**", "/login/**", "/logout").permitAll()
					.requestMatchers("/api/auth/me").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/products", "/api/products/*").permitAll()
					.anyRequest().authenticated()
			}
			.oauth2Login {
				it.userInfoEndpoint { userInfo ->
					userInfo.userService(customOAuth2UserService)
				}
				it.defaultSuccessUrl("/", true)
			}
			.logout {
				it.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
			}
			.exceptionHandling {
				it.authenticationEntryPoint { _, response, _ ->
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
				}
			}
			.build()
	}
}
