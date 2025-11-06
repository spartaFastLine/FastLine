package com.fastline.aiservice.repository;

import com.fastline.aiservice.domain.RequestLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository extends JpaRepository<RequestLog, UUID> {}
