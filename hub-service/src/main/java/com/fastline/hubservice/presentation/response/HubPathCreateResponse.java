package com.fastline.hubservice.presentation.response;

import com.fastline.hubservice.domain.model.HubPath;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

/** 허브 경로 생성 결과 응답 DTO - 생성 직후 클라이언트에 반환 - start/end 허브는 식별자 중심으로 응답 (필요 시 확장) */
public record HubPathCreateResponse(
		UUID hubPathId,
		UUID startHubId,
		UUID endHubId,
		LocalTime duration, // 분 단위 등 정책에 맞게 사용
		Long distance, // km 단위 등 정책에 맞게 사용
		Boolean active,
		Instant createdAt) {
	public static HubPathCreateResponse from(HubPath entity) {
		return new HubPathCreateResponse(
				entity.getHubPathId(),
				entity.getStartHub().getHubId(),
				entity.getEndHub().getHubId(),
				entity.getDuration(),
				entity.getDistance(),
				entity.getActive(),
				entity.getCreatedAt());
	}
}
