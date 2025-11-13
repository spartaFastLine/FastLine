package com.fastline.hubservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info =
				@Info(
						title = "FastLine Hub-Service API",
						version = "v1",
						description = "허브/경로(Hub, HubPath) API 문서"),
		servers = {
			@Server(url = "http://localhost:8080", description = "local"),
		},
		security = {@SecurityRequirement(name = "JWT")},
		tags = {@Tag(name = "Hub"), @Tag(name = "HubPath")})
public class OpenApiConfig {

	@Bean
	public OpenAPI baseOpenAPI() {
		return new OpenAPI()
				.components(
						new Components()
								.addSecuritySchemes(
										"JWT",
										new SecurityScheme()
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")))
				.addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("JWT"))
				.info(
						new io.swagger.v3.oas.models.info.Info()
								.title("FastLine Hub-Service API")
								.version("v1")
								.license(new License().name("Proprietary")));
	}

	// /api/** 만 문서화하고 싶다면 (원하면 패턴 바꿔)
	@Bean
	public GroupedOpenApi hubApis() {
		return GroupedOpenApi.builder()
				.group("hub-service")
				.pathsToMatch("/api/**", "/health/**")
				.build();
	}
}
