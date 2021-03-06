package com.carsync.challenge.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.carsync.challenge.api.auth.AuthInterceptor;

@Configuration
@EnableAspectJAutoProxy
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public AuthInterceptor interceptor() {
		return new AuthInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor()).addPathPatterns("/**").excludePathPatterns("/api/v1/signup",
				"/api/v1/signup/verify-account", "/api/v1/login/two-fa", "/api/v1/login", "/h2-console**",
				"**/api-docs/**", "/swagger-ui/**", "/webjars/**", "/v2/api-docs", "/configuration/ui",
				"/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");

	}

}