package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record PermitRequest(@NotNull Long userId, boolean forced) {
}
