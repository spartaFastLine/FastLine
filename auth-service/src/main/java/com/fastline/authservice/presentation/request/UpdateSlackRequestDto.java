package com.fastline.authservice.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateSlackRequestDto {
    @NotBlank
    private String slackId;
}
