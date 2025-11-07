package com.fastline.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 요청이 처리된 후 응답 반환전 실행
@Component
public class CustomPostFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain
				.filter(exchange)
				.then(
						Mono.fromRunnable(
								() -> { // then() 메서드를 사용하여 응답 후 작업 정의
									// 응답 로깅
									System.out.println("Response Status: " + exchange.getResponse().getStatusCode());
								}));
	}

	@Override
	public int getOrder() {
		return -1; // 필터 순서를 가장 높은 우선 순위로 설정
	}
}
