package com.fastline.messagingservice.domain.repository;

import com.fastline.messagingservice.domain.entity.SlackMessage;

public interface SlackMessageRepository {
    void save(SlackMessage slackMessage);
}
