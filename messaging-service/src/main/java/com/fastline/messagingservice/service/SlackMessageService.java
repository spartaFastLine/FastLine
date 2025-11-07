package com.fastline.messagingservice.service;

import static com.fastline.common.exception.ErrorCode.SEND_SLACK_MESSAGE_FAIL;
import static com.slack.api.webhook.WebhookPayloads.payload;

import com.fastline.common.exception.CustomException;
import com.fastline.messagingservice.domain.SlackMessage;
import com.fastline.messagingservice.dto.SendMessageRequest;
import com.fastline.messagingservice.external.client.AiServiceClient;
import com.fastline.messagingservice.external.dto.MessageGenerationRequest;
import com.fastline.messagingservice.repository.SlackMessageRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackMessageService {

	@Value("${webhook.slack.url}")
	private String SLACK_WEBHOOK_URL;

	private static final String TITLE = "📦 배송 예상 시간 알림";
	private static final DateTimeFormatter DATETIME_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final AiServiceClient aiServiceClient;
	private final Slack slackClient = Slack.getInstance();
	private final SlackMessageRepository slackMessageRepository;

	public void sendMessage(SendMessageRequest orderInfo) {

		MessageGenerationRequest aiRequest = MessageGenerationRequest.from(orderInfo);
		String finalDispatchDeadline =
				aiServiceClient.generate(aiRequest).data().finalDispatchDeadline();
		Attachment attachment = buildSlackAttachment(orderInfo, finalDispatchDeadline);

		try {
			slackClient.send(
					SLACK_WEBHOOK_URL, payload(p -> p.text(TITLE).attachments(List.of(attachment))));
		} catch (IOException e) {
			log.error("[Slack 메시지 전송 실패] orderId: {}", orderInfo.orderId(), e);
			throw new CustomException(SEND_SLACK_MESSAGE_FAIL);
		}

		slackMessageRepository.save(
				SlackMessage.of(orderInfo.orderId(), attachment.getText(), orderInfo.customerEmail()));
	}

	private Attachment buildSlackAttachment(SendMessageRequest request, String aiResponse) {
		StringBuilder sb = new StringBuilder();
		sb.append("주문 번호 : ").append(request.orderId()).append("\n");
		sb.append("주문자 정보 : ")
				.append(request.customerName())
				.append(" / ")
				.append(request.customerEmail())
				.append("\n");
		sb.append("주문 시간 : ").append(formatDateTime(request.orderDateTime())).append("\n");
		sb.append("상품 정보 : ").append(formatItems(request.items())).append("\n");
		sb.append("요청 사항 : ").append(request.requestNote()).append("\n");
		sb.append("발송지 : ").append(request.sourceHub()).append("\n");
		sb.append("경유지 : ").append(formatViaHubs(request.viaHubs())).append("\n");
		sb.append("도착지 : ").append(request.destination()).append("\n");
		sb.append("배송담당자 : ")
				.append(request.deliveryManagerName())
				.append(" / ")
				.append(request.deliveryManagerEmail())
				.append("\n\n");
		sb.append(aiResponse);

		return Attachment.builder().text(sb.toString()).build();
	}

	private String formatItems(List<MessageGenerationRequest.Item> items) {
		if (items == null || items.isEmpty()) {
			return "정보 없음";
		}
		return items.stream()
				.map(i -> i.name() + " " + i.quantity() + "개")
				.collect(Collectors.joining("\n"));
	}

	private String formatViaHubs(List<String> viaHubs) {
		if (viaHubs == null || viaHubs.isEmpty()) {
			return "없음";
		}
		return String.join(", ", viaHubs);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "정보 없음";
		}
		return dateTime.format(DATETIME_FORMATTER);
	}
}
