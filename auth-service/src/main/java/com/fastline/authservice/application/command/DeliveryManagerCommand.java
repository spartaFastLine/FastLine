package com.fastline.authservice.application.command;

import java.util.UUID;

public record DeliveryManagerCommand(
		Integer page,
		Integer size,
		String username,
		UUID hubId,
		String type,
		Long number,
		String status,
		Boolean isActive,
		String sortBy,
		Boolean sortAscending) {}
