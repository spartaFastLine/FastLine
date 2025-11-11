package com.fastline.aiservice.infrastructure.config;

import com.fastline.aiservice.domain.repository.RequestLogRepository;
import com.fastline.aiservice.infrastructure.repository.JpaRequestLogRepository;
import com.fastline.aiservice.infrastructure.repository.RequestLogRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
	@Bean
	public RequestLogRepository orderRepository(JpaRequestLogRepository jpaRequestLogRepository) {
		return new RequestLogRepositoryAdapter(jpaRequestLogRepository);
	}
}
