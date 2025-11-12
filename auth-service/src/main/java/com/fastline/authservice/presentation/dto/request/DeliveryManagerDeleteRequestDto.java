package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeliveryManagerDeleteRequestDto {
	@NotNull private Long userId;
}
