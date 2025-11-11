package com.fastline.messagingservice.infrastructure.repository;

import com.fastline.messagingservice.domain.entity.SlackMessage;
import com.fastline.messagingservice.domain.repository.SlackMessageRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SlackMessageRepositoryAdaptor implements SlackMessageRepository {

    private final JpaSlackMessageRepository jpaSlackMessageRepository;

    @Override
    public void save(SlackMessage slackMessage) {
        jpaSlackMessageRepository.save(slackMessage);
    }
}