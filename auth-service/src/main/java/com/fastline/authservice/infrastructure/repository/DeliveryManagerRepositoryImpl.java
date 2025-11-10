package com.fastline.authservice.infrastructure.repository;

import static com.fastline.authservice.domain.model.QDeliveryManager.deliveryManager;
import static com.fastline.authservice.domain.model.QUser.user;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.DeliveryManagerType;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.DeliveryManagerRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {
	private final JpaDeliveryManagerRepository jpaDeliveryManagerRepository;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public void save(DeliveryManager manager) {
		jpaDeliveryManagerRepository.save(manager);
	}

	@Override
	public long countAll() {
		return jpaDeliveryManagerRepository.count();
	}

	@Override
	public Optional<DeliveryManager> findById(Long userId) {
		return jpaDeliveryManagerRepository.findById(userId);
	}

	@Override
	public Page<DeliveryManager> findDeliveryManagers(
			String username,
			UUID hubId,
			String type,
			Long number,
			String userStatus,
			boolean isActive,
			Pageable pageable) {
		// 결과 데이터
		List<DeliveryManager> deliveryManagers =
				jpaQueryFactory
						.selectFrom(deliveryManager)
						.innerJoin(deliveryManager.user, user)
						.where(
								eqUsername(username),
								eqhubId(hubId),
								eqType(type),
								eqNumber(number),
								eqStatus(userStatus),
								eqIsActive(isActive))
						.orderBy(builderOrderSepifier(pageable))
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.fetch();

		// 전체개수 방환
		Long total =
				jpaQueryFactory
						.select(deliveryManager.count())
						.from(deliveryManager)
						.innerJoin(deliveryManager.user, user)
						.where(
								eqUsername(username),
								eqhubId(hubId),
								eqType(type),
								eqNumber(number),
								eqStatus(userStatus),
								eqIsActive(isActive))
						.fetchOne();
		total = total != null ? total : 0L; // 결과개수가 0이면 null이 반환되므로 0으로 처리
		// 페이지 객체 반환
		return new PageImpl<>(deliveryManagers, pageable, total);
	}

	// 검색 조건 null 처리
	private BooleanExpression eqUsername(String username) {
		return StringUtils.hasText(username) ? user.username.eq(username) : null;
	}

	private BooleanExpression eqhubId(UUID hubId) {
		return hubId != null ? user.hubId.eq(hubId) : null;
	}

	private BooleanExpression eqStatus(String status) {
		return StringUtils.hasText(status) ? user.status.eq(UserStatus.valueOf(status)) : null;
	}

	private BooleanExpression eqType(String type) {
		return StringUtils.hasText(type)
				? deliveryManager.type.eq(DeliveryManagerType.valueOf(type))
				: null;
	}

	private BooleanExpression eqNumber(Long number) {
		return number != null ? deliveryManager.number.eq(number) : null;
	}

	private BooleanExpression eqIsActive(boolean isActive) {
		return isActive ? deliveryManager.deletedAt.isNull() : null;
	}

	// 정렬조건 빌더
	private OrderSpecifier<?>[] builderOrderSepifier(Pageable pageable) {
		List<OrderSpecifier<?>> specs = new ArrayList<>();
		pageable
				.getSort()
				.forEach(
						order -> {
							OrderSpecifier<?> orderSpecifier;
							String property = order.getProperty();
							boolean isAscending = order.isAscending();
							switch (property) {
								case "username" -> orderSpecifier =
										isAscending ? user.username.asc() : user.username.desc();
								case "hubId" -> orderSpecifier = isAscending ? user.hubId.asc() : user.hubId.desc();
								case "type" -> {
									NumberExpression<Integer> typePriority =
											new CaseBuilder()
													.when(deliveryManager.type.eq(DeliveryManagerType.HUB_DELIVERY))
													.then(0)
													.when(deliveryManager.type.eq(DeliveryManagerType.VENDOR_DELIVERY))
													.then(1)
													.otherwise(99);
									orderSpecifier = isAscending ? typePriority.asc() : typePriority.desc();
								}
								case "status" -> {
									NumberExpression<Integer> statusPriority =
											new CaseBuilder()
													.when(user.status.eq(UserStatus.PENDING))
													.then(0)
													.when(user.status.eq(UserStatus.APPROVE))
													.then(1)
													.when(user.status.eq(UserStatus.REJECTED))
													.then(2)
													.when(user.status.eq(UserStatus.SUSPENSION))
													.then(3)
													.when(user.status.eq(UserStatus.DELETED))
													.then(4)
													.otherwise(99);
									orderSpecifier = isAscending ? statusPriority.asc() : statusPriority.desc();
								}
								default -> orderSpecifier =
										isAscending
												? deliveryManager.number.asc()
												: deliveryManager.number.desc(); // 기본 정렬조건 : 배달순번
							}
							specs.add(orderSpecifier);
						});
		return specs.toArray(new OrderSpecifier<?>[0]);
	}
}
