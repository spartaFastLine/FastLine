package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ProductCreateRequest(
		@NotEmpty(message = "상품의 이름은 필수항목 입니다") @Size(max = 30) String name,
		@NotNull(message = "상품의 재고는 필수항목 입니다") @PositiveOrZero(message = "재고는 0개 이상이여야 합니다")
				Integer stock,
		@NotNull(message = "상품의 가격은 필수항목 입니다") @Positive(message = "가격은 1원 이상이여야 합니다") Double price,
		@NotNull(message = "업체의 아이디는 필수항목 입니다") UUID vendorId) {}
