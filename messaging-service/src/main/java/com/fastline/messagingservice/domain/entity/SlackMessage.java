package com.fastline.messagingservice.domain.entity;

import com.fastline.common.auditing.TimeBaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "p_slack_messages")
public class SlackMessage extends TimeBaseEntity<SlackMessage> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID messageId;

	@Column(nullable = false)
	private String slackId;

	@Column(nullable = false)
	private UUID orderId;

	@Column(columnDefinition = "text", nullable = false)
	private String messageContent;

	@Column(nullable = false)
	private Instant sentAt;

	public static SlackMessage of(UUID orderId, String messageContent, String slackId) {
		SlackMessage msg = new SlackMessage();
		msg.orderId = orderId;
		msg.messageContent = messageContent;
		msg.slackId = slackId;
		msg.sentAt = Instant.now();
		return msg;
	}
}
