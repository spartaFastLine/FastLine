package com.fastline.authservice.application.command;

import java.util.UUID;

public record UserSearchCommand(
		Integer page,
		Integer size,
		UUID hubId,
		String username,
		String role,
		String status,
		String sortBy,
		Boolean sortAscending) {}
