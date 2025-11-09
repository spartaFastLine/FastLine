package com.fastline.authservice.presentation.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryManagerSearchRequestDto {
    private Integer page = 1;
    private Integer size = 10;
    private String username;
    private UUID hubId;
    private String type;
    private Long number;
    private String status;   // 사용자 상태
    private String sortBy = "hubId";
    private boolean sortAscending = true;
}
