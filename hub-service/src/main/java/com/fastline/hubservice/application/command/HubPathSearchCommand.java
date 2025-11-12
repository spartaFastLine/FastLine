package com.fastline.hubservice.application.command;

import com.fastline.hubservice.presentation.request.HubPathListRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * 허브 경로 검색 명령 객체 (Command)
 * Controller → Service 계층 전달용
 *
 * - 검색 필터 조건을 불변 형태로 전달
 * - deleted_at IS NULL 조건은 Repository 단에서 자동 필터링 예정
 */
@Getter
@Builder
public class HubPathSearchCommand {

    private final UUID startHubId;
    private final UUID endHubId;
    private final Boolean active;

    public static HubPathSearchCommand from(HubPathListRequest request) {
        return HubPathSearchCommand.builder()
                .startHubId(request.getStartHubId())
                .endHubId(request.getEndHubId())
                .active(request.getActive())
                .build();
    }
}
