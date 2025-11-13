package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.infrastructure.external.dto.ManagerAssignResponse;

public record ManagerAssignResult(Long managerId) {
	public static ManagerAssignResult from(ManagerAssignResponse response) {
		return new ManagerAssignResult(response.managerId());
	}
}
