package com.fastline.authservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PermitRequestDto {
	@NotNull private Long userId;
	private boolean forced;
}
