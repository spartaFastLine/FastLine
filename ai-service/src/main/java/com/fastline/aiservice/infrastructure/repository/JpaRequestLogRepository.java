package com.fastline.aiservice.infrastructure.repository;

import com.fastline.aiservice.domain.entity.RequestLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRequestLogRepository extends JpaRepository<RequestLog, UUID> {}
