package com.fastline.hubservice.application.command;


import java.util.UUID;

/** 허브 목록 조회용 커맨드 (도메인 필터만 포함, 페이지네이션은 Pageable로 분리) */
public record HubSearchCommand(
        String name,
        String address,
        UUID centralHubId,
        Boolean isCentral
) {}
