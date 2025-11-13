package com.fastline.hubservice.presentation.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Summary response DTO for hub path listing. */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HubPathSummaryResponse {

	private Long hubPathId;
	private Long startHubId;
	private String startHubName;
	private Long endHubId;
	private String endHubName;
	private Double distance;
	private Boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
