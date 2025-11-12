package com.fastline.authservice.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @param status   사용자 상태
 * @param isActive 현재 재직 여부
 */
public record DeliveryManagerSearchRequest(Integer page,
										   Integer size,
										   String username,
										   UUID hubId,
										   String type,
										   Long number,
										   String status,
										   @JsonProperty("isActive") Boolean isActive,
										   String sortBy,
										   @JsonProperty("sortAscending") Boolean sortAscending) {
	public DeliveryManagerSearchRequest(Integer page, Integer size, String username, UUID hubId, String type, Long number, String status, Boolean isActive, String sortBy, Boolean sortAscending) {
		this.page = page == null ? 1 : page;
		this.size = size == null ? 10 : size;
		this.username = username;
		this.hubId = hubId;
		this.type = type;
		this.number = number;
		this.status = status;
		this.isActive = isActive != null && isActive;
		this.sortBy = sortBy == null ? "hubId" : sortBy;
		this.sortAscending = sortAscending;
	}
}
