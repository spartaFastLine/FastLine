package com.fastline.hubservice.domain.model;

import com.fastline.common.auditing.TimeBaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hubs")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hub extends TimeBaseEntity<Hub> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "hub_id", nullable = false, updatable = false)
	private UUID hubId;

	@Column(name = "central_hub_id", nullable = true)
	private UUID centralHubId;

	@Column(name = "is_central", nullable = false)
	private boolean isCentral;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	/**
	 * 허브 생성 팩토리 메서드 - 애그리거트 불변식(invariants) 검사 - 외부 레이어의 Command/DTO에 의존하지 않음 (도메인 순수성 유지)
	 *
	 * <p>주의: Application/Web 레이어의 "CreateHubCommand" 같은 DTO를 여기로 넘기지 마세요. 도메인 계층 내부의 명세 객체(Spec/VO)만
	 * 받도록 하여 계층 간 결합을 방지합니다.
	 */
	/** 도메인 내부 응집을 위한 생성 스펙 외부 레이어(Command/DTO)에 의존하지 않도록 Hub 내부에 캡슐화한다. */
	public static record CreateSpec(
			java.util.UUID centralHubId,
			boolean isCentral,
			String name,
			String address,
			Double latitude,
			Double longitude) {}

	public static Hub create(CreateSpec spec) {
		// 기본 검증
		String name = requireNonBlank(spec.name(), "name");
		String address = requireNonBlank(spec.address(), "address");

		Double lat = requireNonNull(spec.latitude(), "latitude");
		Double lon = requireNonNull(spec.longitude(), "longitude");
		/*스팩구간*/
		if (lat < -90.0 || lat > 90.0) {
			throw new IllegalArgumentException("latitude must be between -90 and 90");
		}
		if (lon < -180.0 || lon > 180.0) {
			throw new IllegalArgumentException("longitude must be between -180 and 180");
		}
		/*vo 구간 */
		// 중앙 허브 규칙: 중앙이면 상위 허브가 없어야 한다
		if (spec.isCentral() && spec.centralHubId() != null) {
			throw new IllegalArgumentException("Central hub must not have centralHubId");
		}
		// 일반 허브 규칙: 중앙이 아니면 상위 허브가 있어야 한다
		if (!spec.isCentral() && spec.centralHubId() == null) {
			throw new IllegalArgumentException("Non-central hub requires centralHubId");
		}

		return Hub.builder()
				.hubId(null) // @GeneratedValue(UUID)
				.centralHubId(spec.centralHubId())
				.isCentral(spec.isCentral())
				.name(name)
				.address(address)
				.latitude(lat)
				.longitude(lon)
				.build();
	}

	private static <T> T requireNonNull(T v, String field) {
		if (v == null) throw new IllegalArgumentException(field + " is required");
		return v;
	}

	private static String requireNonBlank(String v, String field) {
		if (v == null || v.trim().isEmpty()) {
			throw new IllegalArgumentException(field + " is required");
		}
		return v.trim();
	}
}
