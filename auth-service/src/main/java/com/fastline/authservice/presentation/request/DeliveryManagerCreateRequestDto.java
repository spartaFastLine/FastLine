package com.fastline.authservice.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeliveryManagerCreateRequestDto {
	@NotNull private Long userId;

	@NotBlank private String type;
}
