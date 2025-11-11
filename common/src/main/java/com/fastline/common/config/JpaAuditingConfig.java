package com.fastline.common.config;

import com.fastline.common.jpa.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaAuditingConfig {
	@Bean
	public AuditorAware<Long> auditorAware() {
		return new SecurityAuditorAware();
	}
}
