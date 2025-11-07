package com.fastline.hubservice.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHubRequest {

	private UUID centralHubId;

	@NotNull private Boolean isCentral;

	@NotBlank private String name;

	@NotBlank private String address;

	@NotNull private Double latitude;

	@NotNull private Double longitude;
}
