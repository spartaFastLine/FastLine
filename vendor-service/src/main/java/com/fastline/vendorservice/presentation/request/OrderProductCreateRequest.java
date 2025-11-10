package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record OrderProductCreateRequest(
		@NotNull(message = "상품의 아이디는 필수항목 입니다") UUID productId, @NotNull @Positive Integer quantity) {}
