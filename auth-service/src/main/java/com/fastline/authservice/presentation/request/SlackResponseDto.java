package com.fastline.authservice.presentation.request;

import lombok.Getter;

@Getter
public class SlackResponseDto {
	private final String slackId;

	public SlackResponseDto(String slackId) {
		this.slackId = slackId;
	}
}
