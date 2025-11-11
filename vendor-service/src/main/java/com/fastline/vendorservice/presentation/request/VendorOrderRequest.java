package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VendorOrderRequest(@NotNull UUID producerId, @NotNull UUID consumerId) {}
