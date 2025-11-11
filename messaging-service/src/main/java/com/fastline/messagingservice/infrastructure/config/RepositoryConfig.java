package com.fastline.messagingservice.infrastructure.config;

import com.fastline.messagingservice.domain.repository.SlackMessageRepository;
import com.fastline.messagingservice.infrastructure.repository.JpaSlackMessageRepository;
import com.fastline.messagingservice.infrastructure.repository.SlackMessageRepositoryAdaptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
	@Bean
	public SlackMessageRepository orderRepository(JpaSlackMessageRepository jpaSlackMessageRepository) {
		return new SlackMessageRepositoryAdaptor(jpaSlackMessageRepository);
	}
}
