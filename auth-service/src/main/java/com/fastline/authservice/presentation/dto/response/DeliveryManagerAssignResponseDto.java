package com.fastline.authservice.presentation.dto.response;

import lombok.Getter;

@Getter
public class DeliveryManagerAssignResponseDto {
	private final Long managerId;

	public DeliveryManagerAssignResponseDto(Long managerId) {
		this.managerId = managerId;
	}
}
