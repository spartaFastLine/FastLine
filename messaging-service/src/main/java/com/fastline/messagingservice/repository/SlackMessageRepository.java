package com.fastline.messagingservice.repository;

import com.fastline.messagingservice.domain.SlackMessage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {}
