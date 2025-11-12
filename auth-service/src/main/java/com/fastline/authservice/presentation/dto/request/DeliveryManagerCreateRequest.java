package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeliveryManagerCreateRequest(@NotNull Long userId,
										   @NotBlank String type) {
}
