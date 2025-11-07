package com.fastline.vendorservice.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record VendorCreateRequest(
		@NotEmpty(message = "업체 이름은 필수항목 입니다") @Size(max = 30) String name,
		@NotEmpty(message = "업체 종류는 필수항목 입니다") String type,
		@NotEmpty(message = "주소는 필수항목 입니다") @Size(max = 30) String city,
		@NotEmpty(message = "주소는 필수항목 입니다") @Size(max = 30) String district,
		@NotEmpty(message = "주소는 필수항목 입니다") @Size(max = 30) String roadName,
		@NotEmpty(message = "주소는 필수항목 입니다")
				@Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리의 숫자로만 이루어져야 합니다")
				String zipCode,
		@NotNull(message = "허브의 아이디는 필수항목 입니다") UUID hubId) {}
