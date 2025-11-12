package com.fastline.authservice.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryManagerSearchRequestDto {
	private Integer page = 1;
	private Integer size = 10;
	private String username;
	private UUID hubId;
	private String type;
	private Long number;
	private String status; // 사용자 상태

	@JsonProperty("isActive")
	private boolean isActive = false; // 현재 재직 여부

	private String sortBy = "hubId";

	@JsonProperty("sortAscending")
	private boolean sortAscending = true;
}
