package com.fastline.authservice.presentation.request;

import lombok.Getter;

@Getter
public class DeliveryManagerAssignResponseDto {
    private final Long managerId;

    public DeliveryManagerAssignResponseDto(Long managerId) {
        this.managerId = managerId;
    }
}
