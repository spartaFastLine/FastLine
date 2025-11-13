package com.fastline.authservice.application.service;

import com.fastline.authservice.application.command.*;
import com.fastline.authservice.application.result.*;
import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.service.UserDomainService;
import com.fastline.authservice.domain.vo.DeliveryManagerType;
import com.fastline.authservice.domain.vo.ManagerOrderBy;
import com.fastline.authservice.domain.vo.UserOrderBy;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDomainService domainService;

	@Transactional
	public void permitSignup(Long managerId, Long userId) {
		User newUser = domainService.userCheck(userId);
		// 매니저 유저 확인
		User manager = domainService.userCheck(managerId);

		// 허브 매니저라면 소속 허브 아이디 체크
		domainService.checkHubManager(manager, newUser.getHubId());
		// 승인 대기 상태가 아닌 경우 예외 발생
		if (newUser.getStatus() != UserStatus.PENDING) throw new CustomException(ErrorCode.NOT_PENDING);

		// 회원가입 승인
		newUser.permitSignup();
	}

	@Transactional(readOnly = true)
	public Page<UserResult> getUsers(Long managerId, UserSearchCommand command) {
		// 매니저 유저 확인
		User manager = domainService.userCheck(managerId);
		UUID requestHubId = command.hubId();
		// 허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
		if (requestHubId != null) {
			domainService.checkHubManager(manager, requestHubId);
		} else {
			// 허브매니저인데 허브아이디가 null인 경우 자기 허브로 고정
			if (manager.getRole() == UserRole.HUB_MANAGER) requestHubId = manager.getHubId();
		}
		// 정렬조건 체크
		UserOrderBy.checkValid(command.sortBy());

		// 오름차순/내림차순
		Sort.Direction directions = command.sortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable =
				PageRequest.of(command.page() - 1, command.size(), Sort.by(directions, command.sortBy()));
		Page<User> users =
				userRepository.findUsers(
						command.username(), requestHubId, command.role(), command.status(), pageable);

		return users.map(UserResult::new);
	}

	// 배달 매니저 검색
	public Page<DeliveryManagerResult> getDeliveryManagers(
			Long managerId, DeliveryManagerCommand command) {
		// 매니저 유저 확인
		User manager = domainService.userCheck(managerId);
		UUID hubId = command.hubId();
		// 허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
		if (hubId != null) domainService.checkHubManager(manager, hubId);
		else {
			// 허브매니저인데 허브아이디가 null인 경우 자기 허브로 고정
			if (manager.getRole() == UserRole.HUB_MANAGER) hubId = manager.getHubId();
		}
		// 정렬조건 체크
		ManagerOrderBy.checkValid(command.sortBy());

		// 오름차순/내림차순
		Sort.Direction directions = command.sortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable pageable =
				PageRequest.of(command.page() - 1, command.size(), Sort.by(directions, command.sortBy()));

		Page<DeliveryManager> deliveryManagers =
				userRepository.findDeliveryManagers(
						command.username(),
						hubId,
						command.type(),
						command.number(),
						command.status(),
						command.isActive(),
						pageable);
		return deliveryManagers.map(DeliveryManagerResult::new);
	}

	@Transactional(readOnly = true)
	public UserResult getUser(Long userId) {
		// 유저 확인
		User user = domainService.userCheck(userId);
		return new UserResult(user);
	}

	@Transactional
	public void updatePassword(Long userId, UpdatePasswordCommand command) {
		// 유저 확인
		User user = domainService.userCheck(userId);
		// 현재 비밀번호 확인
		if (!passwordEncoder.matches(command.password(), user.getPassword()))
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHES);

		// 동일한 비밀번호인지 확인
		if (passwordEncoder.matches(command.newPassword(), user.getPassword()))
			throw new CustomException(ErrorCode.PASSWORD_EQUAL);

		// 새 비밀번호 인코딩 및 업데이트
		user.updatePassword(passwordEncoder.encode(command.newPassword()));
	}

	@Transactional
	public void updateSlack(Long userId, UpdateSlackCommand command) {
		// 유저 확인
		User user = domainService.userCheck(userId);
		// 슬랙 아이디 업데이트
		user.updateSlackId(command.slackId());
	}

	@Transactional
	public void withdrawUser(Long userId) {
		// 유저 확인
		User user = domainService.userCheck(userId);
		// 권한 정지
		user.updateReject();
	}

	// 관리자가 유저 정보 수정
	@Transactional
	public void updateDeliveryManager(Long managerId, Long userId, UserManagerUpdateCommand command) {
		// 매니저 유저 확인
		User manager = domainService.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = domainService.userCheck(userId);

		// 허브 매니저라면 소속 허브 아이디 체크
		domainService.checkHubManager(manager, user.getHubId());

		// 도메인 서비스에서 유저 정보 수정
		user.updateByManager(
				command.hubId(),
				UserStatus.from(command.status()),
				DeliveryManagerType.from(command.deliveryType()));
	}

	@Transactional
	public void permitDeleteUser(Long managerId, Long userId) {
		// 매니저 유저 확인
		User manager = domainService.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = domainService.userCheck(userId);

		// 허브 매니저라면 소속 허브 아이디 체크
		domainService.checkHubManager(manager, user.getHubId());

		// 권한 정지 상태가 아니라면 예외 발생
		if (user.getStatus() != UserStatus.REJECTED) throw new CustomException(ErrorCode.NOT_REJECTED);
		// 탈퇴 신청한 유저 삭제
		user.delete();
	}

	public DeliveryManagerMessageResult getDeliveryManagerMessageInfo(Long userId) {
		User user = domainService.userCheck(userId);
		return new DeliveryManagerMessageResult(user.getSlackId(), user.getUsername(), user.getEmail());
	}

	public UserHubIdResult getUserHubInfo(Long userId) {
		User user = domainService.userCheck(userId);
		return new UserHubIdResult(user.getHubId());
	}

	// 배달 매니저 자동 배정
	@Transactional
	public DeliveryManagerAssignResult getDeliveryManagerAssignment(
			String hubId, String managerType) {
		User manager =
				userRepository
						.assignDeliveryManager(UUID.fromString(hubId), DeliveryManagerType.from(managerType))
						.orElseThrow(() -> new CustomException(ErrorCode.IMPOSSIBLE_ASSIGNMENT));
		manager.assign();
		return new DeliveryManagerAssignResult(manager.getId());
	}

	// 배달매니저 배송완료 알림 -> 배송가능 상태로 변경
	@Transactional
	public void completeDeliveryManager(Long managerId) {
		User manager = domainService.userCheck(managerId);
		manager.complete();
	}
}
