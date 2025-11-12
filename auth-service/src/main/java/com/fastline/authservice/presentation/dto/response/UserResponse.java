package com.fastline.authservice.presentation.dto.response;

import java.util.UUID;

public record UserResponse(Long userId,
						   String email,
						   String username,
						   String password,
						   String role,
						   String slackId,
						   String status,
						   UUID hubId) {
}
