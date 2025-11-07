package com.fastline.authservice.presentation.request;

import com.fastline.authservice.domain.model.User;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserResponseDto {
	private final Long userId;
	private final String email;
	private final String username;
	private final String password;
	private final String role;
	private final String slackId;
	private final String status;
	private final UUID hubId;

	public UserResponseDto(User user) {
		this.userId = user.getId();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.role = user.getRole().name();
		this.slackId = user.getSlackId();
		this.status = user.getStatus().name();
		this.hubId = user.getHubId() != null ? user.getHubId() : null;
	}
}
