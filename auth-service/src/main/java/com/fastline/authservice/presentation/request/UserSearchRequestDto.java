package com.fastline.authservice.presentation.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserSearchRequestDto {
	private Integer page = 1;
	private Integer size = 10;
	private UUID hubId;
	private String username;
	private String role;
	private String status;
	private String sortBy = "username";
	@JsonProperty("sortAscending")
	private boolean sortAscending = true;
}
