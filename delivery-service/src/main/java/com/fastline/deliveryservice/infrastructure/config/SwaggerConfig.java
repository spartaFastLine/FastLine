package com.fastline.deliveryservice.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().components(new Components()).info(apiInfo());
	}

	private Info apiInfo() {
		return new Info().title("DELIVERY API").description("배송 관련 API").version("1.0.0");
	}
}
