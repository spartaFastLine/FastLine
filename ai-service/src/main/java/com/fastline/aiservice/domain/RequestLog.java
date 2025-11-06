 package com.fastline.aiservice.domain;

 import com.fastline.common.jpa.TimeBaseEntity;
 import jakarta.persistence.Column;
 import jakarta.persistence.Entity;
 import jakarta.persistence.Id;
 import jakarta.persistence.Table;
 import java.util.UUID;
 import lombok.Getter;

 @Getter
 @Entity
 @Table(name = "p_ai_request_log")
 public class RequestLog extends TimeBaseEntity {
	@Id private UUID requestId;

	@Column(nullable = false)
	private UUID orderId;

	@Column(nullable = false)
	private RequestStatus status;
 }
