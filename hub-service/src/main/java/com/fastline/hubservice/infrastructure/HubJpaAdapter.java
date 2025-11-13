package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.repository.HubRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

/**
 * HubRepository JPA 구현체 - 도메인 레이어의 HubRepository 인터페이스 구현 - 실제 DB 접근은 Spring Data JpaRepository 위임
 */
@Repository
@RequiredArgsConstructor
public class HubJpaAdapter implements HubRepository {

	private final HubJpaRepository hubJpaRepository;

	@Override
	public Optional<Hub> findActiveById(UUID hubId) {
		return hubJpaRepository.findByHubIdAndDeletedAtIsNull(hubId);
	}

	@Override
	public boolean existsActiveById(UUID hubId) {
		return hubJpaRepository.existsByHubIdAndDeletedAtIsNull(hubId);
	}

	@Override
	public UUID findCentralIdOf(UUID hubId) {
		return hubJpaRepository.findCentralHubIdByHubId(hubId);
	}

	@Override
	public List<Hub> findAllActive() {
		return hubJpaRepository.findAllByDeletedAtIsNull();
	}

	@Override
	public Hub save(Hub hub) {
		return hubJpaRepository.save(hub);
	}

	@Override
	public Optional<Object> findById(UUID hubId) {
		return (Optional) hubJpaRepository.findById(hubId);
	}

	@Override
	public long count(Specification<Hub> childSpec) {
		return hubJpaRepository.count(childSpec);
	}

	@Override
	public Page<Hub> findAll(Specification<Hub> spec, Pageable pageable) {
		return hubJpaRepository.findAll(spec, pageable);
	}
}
