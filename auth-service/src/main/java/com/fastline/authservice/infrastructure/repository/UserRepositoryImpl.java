package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserRole;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fastline.authservice.domain.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }
    @Override
    public Optional<User> findByUsername(String username) {return jpaUserRepository.findByUsername(username); }
    @Override
    public Optional<User> findById(Long id) {return jpaUserRepository.findById(id); }

    @Override
    public void save(User user) {jpaUserRepository.save(user);}

    @Override
    public Page<User> findUsers(String username, UUID hubId, String role, String status, Pageable pageable) {

        List<User> users = jpaQueryFactory.selectFrom(user)
                .where(
                        eqUsername(username),
                        eqhubId(hubId),
                        eqRole(role),
                        eqStatus(status)
                )
                .orderBy(builderOrderSepifier(pageable))  //정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 개수 반환
        Long total = jpaQueryFactory.select(user.count())
                .from(user)
                .where(
                        eqUsername(username),
                        eqhubId(hubId),
                        eqRole(role),
                        eqStatus(status)
                )
                .fetchOne();
        return new PageImpl<>(users, pageable, total);
    }

    //검색 조건 null 처리
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

    //정렬조건 빌더
    private OrderSpecifier<?>[] builderOrderSepifier(Pageable pageable) {
        List<OrderSpecifier<?>> specs = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            OrderSpecifier<?> orderSpecifier;
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            if(property.equals("username")) {
                orderSpecifier = isAscending? user.username.asc() : user.username.desc();
            } else if (property.equals("role")) {
                NumberExpression<Integer> rolePriority = new CaseBuilder()
                        .when(user.role.eq(UserRole.MASTER)).then(0)
                        .when(user.role.eq(UserRole.HUB_MANAGER)).then(1)
                        .when(user.role.eq(UserRole.DELIVERY_MANAGER)).then(2)
                        .when(user.role.eq(UserRole.VENDOR_MANAGER)).then(3)
                        .otherwise(99);
                orderSpecifier = isAscending? rolePriority.asc() : rolePriority.desc();
            } else if (property.equals("status")) {
                NumberExpression<Integer> statusPriority = new CaseBuilder()
                        .when(user.status.eq(UserStatus.PENDING)).then(0)
                        .when(user.status.eq(UserStatus.APPROVE)).then(1)
                        .when(user.status.eq(UserStatus.REJECTED)).then(2)
                        .when(user.status.eq(UserStatus.SUSPENSION)).then(3)
                        .when(user.status.eq(UserStatus.DELETED)).then(4)
                        .otherwise(99);
                orderSpecifier = isAscending? statusPriority.asc() : statusPriority.desc();
            } else {
                //정렬조건은 이미 체크했으니 남은건 hubId로 간주
                orderSpecifier = isAscending? user.hubId.asc() : user.hubId.desc();
            }
            specs.add(orderSpecifier);
        });
        return specs.toArray(new OrderSpecifier<?>[0]);


    }




}
