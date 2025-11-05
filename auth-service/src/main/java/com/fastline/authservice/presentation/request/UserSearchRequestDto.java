package com.fastline.authservice.presentation.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserSearchRequestDto {
    private Integer page;
    private Integer size;
    private UUID hubId;
    private String username;
    private String role;
    private String status;
    private String sortBy;
    private boolean sortAscending;
}
