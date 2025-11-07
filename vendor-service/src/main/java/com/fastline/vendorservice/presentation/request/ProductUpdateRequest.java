package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        @Size(max = 30)
        String name,
        @PositiveOrZero
        Integer stock,
        @Positive
        Double price
) {
}
