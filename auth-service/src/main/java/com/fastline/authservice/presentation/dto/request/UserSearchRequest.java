package com.fastline.authservice.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserSearchRequest(
		Integer page,
		Integer size,
		UUID hubId,
		String username,
		String role,
		String status,
		String sortBy,
		@JsonProperty("sortAscending") Boolean sortAscending) {
	public UserSearchRequest(
			Integer page,
			Integer size,
			UUID hubId,
			String username,
			String role,
			String status,
			String sortBy,
			Boolean sortAscending) {
		this.page = page == null ? 1 : page;
		this.size = size == null ? 10 : size;
		this.hubId = hubId;
		this.username = username;
		this.role = role;
		this.status = status;
		this.sortBy = sortBy == null ? "username" : sortBy;
		this.sortAscending = sortAscending == null || sortAscending;
	}
}
