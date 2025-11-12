package com.fastline.hubservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 허브 간 이동경로 생성 명령 객체 (Command)
 * Controller → Service 계층 전달용
 *
 * - 도메인 친화적 불변 객체
 * - Command는 Request DTO와 달리 검증/변환 이후 안전한 상태를 보장
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathCreateCommand{
    private UUID startHubId;
    private UUID endHubId;
    private Boolean active;
}
