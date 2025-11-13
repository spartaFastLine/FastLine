package com.fastline.authservice.infrastructure.repository;

import static com.fastline.authservice.domain.model.QUser.user;
import static com.fastline.authservice.domain.model.QDeliveryManager.deliveryManager;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.vo.DeliveryManagerType;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.common.security.model.UserRole;
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
public class UserRepositoryAdapter implements UserRepository {
	private final JpaUserRepository jpaUserRepository;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<User> findUserByEmail(String email) {
		return jpaUserRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		return jpaUserRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return jpaUserRepository.findById(id);
	}

	@Override
	public void save(User user) {
		jpaUserRepository.save(user);
	}

	@Override
	public Long countDeliveryManagers() {
		return jpaUserRepository.countDeliveryManagers();
	}

	@Override
	public Page<User> findUsers(
			String username, UUID hubId, String role, String status, Pageable pageable) {

		List<User> users =
				jpaQueryFactory
						.selectFrom(user)
						.where(eqUsername(username), eqhubId(hubId), eqRole(role), eqStatus(status))
						.orderBy(builderUserOrderSepifier(pageable)) // 정렬
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.fetch();

		// 전체 개수 반환
		Long total =
				jpaQueryFactory
						.select(user.count())
						.from(user)
						.where(eqUsername(username), eqhubId(hubId), eqRole(role), eqStatus(status))
						.fetchOne();
		total = total != null ? total : 0L; // 결과개수가 0이면 null이 반환되므로 0으로 처리
		return new PageImpl<>(users, pageable, total);
	}


	@Override
	public Page<DeliveryManager> findDeliveryManagers(String username, UUID hubId, String deliveryType, Long number, String userStatus, boolean isActive, Pageable pageable) {
		List<DeliveryManager> deliveryManagers =
				jpaQueryFactory.selectFrom(deliveryManager)
						.innerJoin(deliveryManager.user, user)
						.where(
								eqUsername(username),
								eqhubId(hubId),
								eqType(deliveryType),
								eqNumber(number),
								eqStatus(userStatus),
								eqIsActive(isActive))
						.orderBy(builderDeliveryOrderSepifier(pageable))
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.fetch();
		Long total = jpaQueryFactory.select(user.count())
				.from(user)
				.innerJoin(user.deliveryManager,deliveryManager)
				.where(
						eqUsername(username),
						eqhubId(hubId),
						eqType(deliveryType),
						eqNumber(number),
						eqStatus(userStatus),
						eqIsActive(isActive))
				.fetchOne();
		total = total != null ? total : 0L;
		return new PageImpl<>(deliveryManagers, pageable, total);
	}

	// 검색 조건 null 처리
	private BooleanExpression eqUsername(String username) {
		return StringUtils.hasText(username) ? user.username.eq(username) : null;
	}

	private BooleanExpression eqhubId(UUID hubId) {
		return hubId != null ? user.hubId.eq(hubId) : null;
	}

	private BooleanExpression eqRole(String role) {
		return StringUtils.hasText(role) ? user.role.eq(UserRole.valueOf(role)) : null;
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
		return isActive ? user.deletedAt.isNull() : null;
	}

	// 정렬조건 빌더
	private OrderSpecifier<?>[] builderUserOrderSepifier(Pageable pageable) {
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
								case "role" -> {
									NumberExpression<Integer> rolePriority =
											new CaseBuilder()
													.when(user.role.eq(UserRole.MASTER))
													.then(0)
													.when(user.role.eq(UserRole.HUB_MANAGER))
													.then(1)
													.when(user.role.eq(UserRole.DELIVERY_MANAGER))
													.then(2)
													.when(user.role.eq(UserRole.VENDOR_MANAGER))
													.then(3)
													.otherwise(99);
									orderSpecifier = isAscending ? rolePriority.asc() : rolePriority.desc();
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
								default ->
								// 정렬조건은 이미 체크했으니 남은건 hubId로 간주
								orderSpecifier = isAscending ? user.hubId.asc() : user.hubId.desc();
							}
							specs.add(orderSpecifier);
						});
		return specs.toArray(new OrderSpecifier<?>[0]);
	}
	// 정렬조건 빌더
	private OrderSpecifier<?>[] builderDeliveryOrderSepifier(Pageable pageable) {
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
