package com.fastline.hubservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Represents a request to create a hub path between two hubs. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathCreateRequest {

	@NotNull private UUID startHubId;

	@NotNull private UUID endHubId;

	@NotNull private Long distance;

	private Boolean active;
}
