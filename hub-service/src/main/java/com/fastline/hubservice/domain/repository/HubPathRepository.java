package com.fastline.hubservice.domain.repository;

import com.fastline.hubservice.domain.model.HubPath;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** HubPath 리포지토리 - 소프트 삭제(deletedAt) 기반 활성 데이터 전용 편의 메서드 제공 - 검색 최적화를 위해 Specification도 지원 */
@Repository
public interface HubPathRepository {

	// ----- 기본 조회 (소프트 삭제 제외) -----
	Optional<HubPath> findByHubPathIdAndDeletedAtIsNull(UUID hubPathId);

	boolean existsByHubPathIdAndDeletedAtIsNull(UUID hubPathId);

	List<HubPath> findAllByDeletedAtIsNull();

	// 시작/도착 허브 기준 단건
	Optional<HubPath> findByStartHub_HubIdAndEndHub_HubIdAndDeletedAtIsNull(
			UUID startHubId, UUID endHubId);

	// ----- 편의 default 메서드 -----
	default Optional<HubPath> findActiveById(UUID hubPathId) {
		return findByHubPathIdAndDeletedAtIsNull(hubPathId);
	}

	default Optional<HubPath> findActiveByStartAndEnd(UUID startHubId, UUID endHubId) {
		return findByStartHub_HubIdAndEndHub_HubIdAndDeletedAtIsNull(startHubId, endHubId);
	}

	default List<HubPath> findAllActive() {
		return findAllByDeletedAtIsNull();
	}

	HubPath save(HubPath hubPath);
}
