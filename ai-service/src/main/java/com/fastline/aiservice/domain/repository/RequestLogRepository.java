package com.fastline.aiservice.domain.repository;

import com.fastline.aiservice.domain.entity.RequestLog;

public interface RequestLogRepository {
	RequestLog save(RequestLog requestLog);
}
