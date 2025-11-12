package com.fastline.authservice.presentation.request;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UserHubIdResponseDto {
	private final UUID hubId;

	public UserHubIdResponseDto(UUID hubId) {
		this.hubId = hubId;
	}
}
