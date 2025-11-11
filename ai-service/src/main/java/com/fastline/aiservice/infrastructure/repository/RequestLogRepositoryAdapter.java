package com.fastline.aiservice.infrastructure.repository;

import com.fastline.aiservice.domain.entity.RequestLog;
import com.fastline.aiservice.domain.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestLogRepositoryAdapter implements RequestLogRepository {

	private final JpaRequestLogRepository jpaRequestLogRepository;

	@Override
	public RequestLog save(RequestLog order) {
		return jpaRequestLogRepository.save(order);
	}
}
