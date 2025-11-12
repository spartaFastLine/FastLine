package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeliveryManagerDeleteRequest(@NotNull Long userId) {
}
