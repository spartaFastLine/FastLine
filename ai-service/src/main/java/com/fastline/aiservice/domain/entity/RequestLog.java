package com.fastline.aiservice.domain.entity;

import com.fastline.common.auditing.BaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "p_ai_request_log")
public class RequestLog extends BaseEntity<RequestLog> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID requestId;

	@Column(nullable = false)
	private UUID orderId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RequestStatus status;

	@Column(columnDefinition = "text")
	private String rawPrompt;

	@Column(columnDefinition = "text")
	private String rawResponse;

	protected RequestLog() {}

	public static RequestLog ofRequested(UUID orderId) {
		RequestLog log = new RequestLog();
		log.orderId = orderId;
		log.status = RequestStatus.REQUESTED;
		return log;
	}

	public void success(String rawPrompt, String rawResponse) {
		this.status = RequestStatus.SUCCESS;
		this.rawPrompt = rawPrompt;
		this.rawResponse = rawResponse;
	}

	public void fail(String rawPrompt, String rawResponse) {
		this.status = RequestStatus.FAIL;
		this.rawPrompt = rawPrompt;
		this.rawResponse = rawResponse;
	}
}
