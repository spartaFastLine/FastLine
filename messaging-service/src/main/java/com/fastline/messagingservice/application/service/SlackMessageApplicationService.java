package com.fastline.messagingservice.application.service;

import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.application.formatter.SlackMessageFormatter;
import com.fastline.messagingservice.domain.entity.SlackMessage;
import com.fastline.messagingservice.domain.repository.SlackMessageRepository;
import com.fastline.messagingservice.infrastructure.slack.SlackWebhookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackMessageApplicationService {

	private final AiClient aiClient;
	private final SlackWebhookClient slackWebhookClient;
	private final SlackMessageRepository slackMessageRepository;

	@Transactional
	public void send(SendMessageCommand cmd) {

		String finalDispatchDeadline = aiClient.generate(cmd);

		String messageText = SlackMessageFormatter.format(cmd, finalDispatchDeadline);

		slackWebhookClient.send(messageText);

		slackMessageRepository.save(SlackMessage.of(cmd.orderId(), messageText, cmd.customerEmail()));
	}
}
