package com.fastline.gatewayservice.filter;

import java.util.logging.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 요청 처리전 실행되는 필터
@Component
public class CustomPreFilter implements GlobalFilter, Ordered {

	private static final Logger logger = Logger.getLogger(CustomPreFilter.class.getName());

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		logger.info("PreFilter : request URI: " + request.getURI());
		return chain.filter(exchange);
	}

	// 필터의 우선순위를 지정하는 메서드(값이 작을수록 먼저 실행)
	@Override
	public int getOrder() {
		return -1; // 필터 순서를 가장 높은 우선 순위로 설정
	}
}
