package com.fastline.messagingservice.infrastructure.slack;

import static com.fastline.common.exception.ErrorCode.SEND_SLACK_MESSAGE_FAIL;
import static com.slack.api.webhook.WebhookPayloads.payload;

import com.fastline.common.exception.CustomException;
import com.slack.api.Slack;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackWebhookClient {

	@Value("${webhook.slack.url}")
	private String SLACK_WEBHOOK_URL;

	private final Slack slackClient = Slack.getInstance();

	public void send(String text) {
		try {
			slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p.text("📦 배송 예상 시간 알림").text(text)));
		} catch (IOException e) {
			throw new CustomException(SEND_SLACK_MESSAGE_FAIL);
		}
	}
}
