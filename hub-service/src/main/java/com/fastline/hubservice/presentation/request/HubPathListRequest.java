package com.fastline.hubservice.presentation.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** DTO for filtering and paginating hub path search results. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathListRequest {
	private UUID startHubId;
	private UUID endHubId;
	private Boolean active;
	private String startHubName;
	private String endHubName;
}
