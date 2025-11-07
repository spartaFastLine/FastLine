package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record VendorUpdateRequest(
		@Size(max = 30) String name,
		String type,
		@Size(max = 30) String city,
		@Size(max = 30) String district,
		@Size(max = 30) String roadName,
		@Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리의 숫자로만 이루어져야 합니다") String zipCode,
		UUID hubId) {}
