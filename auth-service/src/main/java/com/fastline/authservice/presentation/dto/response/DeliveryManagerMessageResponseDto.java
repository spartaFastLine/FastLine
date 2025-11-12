package com.fastline.authservice.presentation.dto.response;

import lombok.Getter;

@Getter
public class DeliveryManagerMessageResponseDto {
	private final String slackId;
	private final String username;
	private final String email;

	public DeliveryManagerMessageResponseDto(String slackId, String username, String email) {
		this.slackId = slackId;
		this.username = username;
		this.email = email;
	}
}
