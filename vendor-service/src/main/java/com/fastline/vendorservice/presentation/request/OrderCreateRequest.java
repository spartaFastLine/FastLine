package com.fastline.vendorservice.presentation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
		@NotNull UUID vendorProducerId,
		@NotNull UUID vendorConsumerId,
		String request,
		@NotNull @Size(min = 1) List<@Valid OrderProductCreateRequest> orderProductRequests) {}
