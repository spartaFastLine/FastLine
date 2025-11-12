package com.fastline.hubservice.infrastructure.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-NCP-APIGW-API-KEY-ID", System.getenv("CLIENT_ID"));
            requestTemplate.header("X-NCP-APIGW-API-KEY", System.getenv("CLIENT_SECRET"));
        };
    }
}
