package com.fastline.vendorservice.infrastructure.external.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OrderCompleteDto(@NotNull LocalDateTime arrivedTime) {}
