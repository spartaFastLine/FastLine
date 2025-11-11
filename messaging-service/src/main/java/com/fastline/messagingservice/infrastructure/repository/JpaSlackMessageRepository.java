package com.fastline.messagingservice.infrastructure.repository;

import com.fastline.messagingservice.domain.entity.SlackMessage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSlackMessageRepository extends JpaRepository<SlackMessage, UUID> {}
