package com.fastline.authservice.application.change;

import com.fastline.authservice.domain.model.*;
import com.fastline.authservice.domain.repository.DeliveryManagerRepository;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.vo.DeliveryManagerType;
import com.fastline.authservice.domain.vo.ManagerOrderBy;
import com.fastline.authservice.domain.vo.UserOrderBy;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.authservice.application.service.HubClient;
import com.fastline.authservice.presentation.dto.request.*;
import com.fastline.authservice.presentation.dto.response.*;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.common.security.model.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final CheckUser checkUser;

	// 배달 매니저 생성 - 마스터, 허브 관리자만 가능
	// todo : 사용자 승인시 동시 진행 - redis
	// todo  : requestDto의 배달 가능한 허브 아이디가 진짜 허브 아이디가 맞는지 확인
	@Transactional
	public void createDeliveryManager(Long managerId, DeliveryManagerCreateRequest requestDto) {
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.userId());
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);

		// 이미 배달 매니저로 등록된 유저인지 확인
		if (deliveryManagerRepository.findById(user.getId()).isPresent())
			throw new CustomException(ErrorCode.EXIST_DELIVERY_MANAGER);
		// 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
		checkUser.checkHubManager(manager, user.getHubId());
		// 회원 승인시 동시 진행
		if (user.getStatus() != UserStatus.PENDING) throw new CustomException(ErrorCode.NOT_PENDING);

		// 회원이 배달매니저인지 확인
		checkDeliveryManager(user);

		// 배송 순번은 현재 배달 매니저 수 +1
		long number = deliveryManagerRepository.countAll();

		// 배달 매니저 생성
		DeliveryManager deliveryManager =
				new DeliveryManager(user, DeliveryManagerType.valueOf(requestDto.type()), number + 1);

		// 배달 매니저 저장
		deliveryManagerRepository.save(deliveryManager);
	}

	// 배달 매니저 단건 조회 - 배달 매니저 본인만 가능, 마스터나 허브 관리자는 다건 조회에서 가능
	@Transactional(readOnly = true)
	public DeliveryManagerResponse getDeliveryManager(Long userId) {
		// 정보 확인
		DeliveryManager manager = findDeliveryManager(userId);
		return new DeliveryManagerResponse(manager);
	}

	// 배달 매니저 검색
	public Page<DeliveryManagerResponse> getDeliveryManagers(
			Long managerId, @Valid DeliveryManagerSearchRequest requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		UUID hubId = requestDto.hubId();
		// 허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
		if (hubId != null) checkUser.checkHubManager(manager, hubId);
		else {
			// 허브매니저인데 허브아이디가 null인 경우 자기 허브로 고정
			if (manager.getRole() == UserRole.HUB_MANAGER) hubId = manager.getHubId();
		}
		// 정렬조건 체크
		ManagerOrderBy.checkValid(requestDto.sortBy());

		// 오름차순/내림차순
		Sort.Direction directions =
				requestDto.sortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable pageable =
				PageRequest.of(
						requestDto.page() - 1,
						requestDto.size(),
						Sort.by(directions, requestDto.sortBy()));

		Page<DeliveryManager> deliveryManagers =
				deliveryManagerRepository.findDeliveryManagers(
						requestDto.username(),
						hubId,
						requestDto.type(),
						requestDto.number(),
						requestDto.status(),
						requestDto.isActive(),
						pageable);
		return deliveryManagers.map(DeliveryManagerResponse::new);
	}

	@Transactional
	public void updateDeliveryManager(
			Long managerId, @Valid DeliveryManagerCreateRequest requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.userId());

		// 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
		checkUser.checkHubManager(manager, user.getHubId());

		// 회원이 배달매니저인지 확인
		checkDeliveryManager(user);

		DeliveryManager deliveryManager = findDeliveryManager(user.getId());
		if (deliveryManager.getDeletedAt() != null)
			throw new CustomException(ErrorCode.DELIVERY_MANAGER_DELETED);
		// 배달 매니저 타입 변경
		deliveryManager.updateType(DeliveryManagerType.valueOf(requestDto.type()));
	}

	@Transactional
	public void deleteDeliveryManager(
			Long managerId, @Valid DeliveryManagerDeleteRequest requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.userId());

		// 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
		checkUser.checkHubManager(manager, user.getHubId());

		// 배달 매니저 삭제
		DeliveryManager deliveryManager = findDeliveryManager(user.getId());
		deliveryManager.delete(manager.getId());
	}

	// 배달 매니저 자동 배정
	@Transactional
	public DeliveryManagerAssignResponse getDeliveryManagerAssignment(
			String hubId, String managerType) {
		DeliveryManager manager =
				deliveryManagerRepository
						.assignDeliveryManager(UUID.fromString(hubId), DeliveryManagerType.valueOf(managerType))
						.orElseThrow(() -> new CustomException(ErrorCode.IMPOSSIBLE_ASSIGNMENT));
		manager.assign();
		return new DeliveryManagerAssignResponse(manager.getId());
	}

	// 배달매니저 배송완료 알림 -> 배송가능 상태로 변경
	@Transactional
	public void completeDeliveryManager(Long managerId) {
		DeliveryManager manager = findDeliveryManager(managerId);
		manager.complete();
	}

	private static void checkDeliveryManager(User user) {
		if (user.getRole() != UserRole.DELIVERY_MANAGER)
			throw new CustomException(ErrorCode.NOT_DELIVERY_MANAGER);
	}

	private DeliveryManager findDeliveryManager(Long userId) {
		return deliveryManagerRepository
				.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));
	}

	@Service
	@RequiredArgsConstructor
	public static class AuthService {
		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;
		private final JwtUtil jwtUtil;
		private final CheckUser checkUser;

		// todo : 허브여부체크 다시 풀기
		@Transactional
		public void signup(SignupRequest requestDto) {
			String email = requestDto.email();
			String username = requestDto.username();

			// 회원 중복 확인 - 코드 다시
			userRepository
					.findByEmail(email)
					.ifPresent(
							user -> {
								throw new CustomException(ErrorCode.EXIST_EMAIL);
							});

			// 회원 username 중복 확인
			userRepository
					.findByUsername(username)
					.ifPresent(
							user1 -> {
								throw new CustomException(ErrorCode.EXIST_USERNAME);
							});

			// 사용자 ROLE 확인
			UserRole role = UserRole.valueOf(requestDto.roll());
			String password = passwordEncoder.encode(requestDto.password());

			// 소속된 허브아이디 확인
			//		checkUser.checkHubExist(requestDto.getHubId());

			// 새로운 사용자 생성 및 저장
			User user =
					new User(email, username, password, role, requestDto.hubId(), requestDto.slackId());
			userRepository.save(user);
		}

		public void login(@Valid LoginRequest requestDto, HttpServletResponse res) {
			String username = requestDto.username();
			String password = requestDto.password();

			User user =
					userRepository
							.findByUsername(username)
							.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHES);
			}

			// 상태가 APPROVED가 아닌 경우 예외 발생
			if (user.getStatus() != UserStatus.APPROVE)
				throw new CustomException(ErrorCode.USER_NOT_APPROVE);
			// JWT 토큰 생성 및 응답 헤더에 추가
			String token = jwtUtil.createToken(user.getId(), user.getRole().toString());
			res.setHeader("Authorization", token);
		}
	}

	@Component
	@RequiredArgsConstructor
	public static class CheckUser {
		private final UserRepository userRepository;
		private final HubClient hubClient;

		public User userCheck(Long userId) {
			return userRepository
					.findById(userId)
					.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		}

		// 허브 매니저라면 소속 허브 아이디 체크
		public void checkHubManager(User manager, UUID requestHubId) {
			if (manager.getRole() == UserRole.HUB_MANAGER && !manager.getHubId().equals(requestHubId))
				throw new CustomException(ErrorCode.NOT_HUB_MANAGER);
		}

		public void checkHubExist(@NotNull UUID hubId) {
			HubExistRequest hubExist = hubClient.getHubExists(hubId);
			if (!hubExist.exist()) {
				throw new CustomException(ErrorCode.HUB_NOT_FOUND);
			}
		}
	}

	@Service
	@RequiredArgsConstructor
	public static class UserService {
		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;
		private final CheckUser checkUser;

		// 회원가입 승인
		@Transactional
		public void permitSignup(Long managerId, @Valid PermitRequest requestDto) {
			User newUser = checkUser.userCheck(requestDto.userId());
			// 매니저 유저 확인
			User manager = checkUser.userCheck(managerId);

			// 허브 매니저라면 소속 허브 아이디 체크
			checkUser.checkHubManager(manager, newUser.getHubId());
			// 승인 대기 상태가 아닌 경우 예외 발생
			if (newUser.getStatus() != UserStatus.PENDING) throw new CustomException(ErrorCode.NOT_PENDING);

			// 회원가입 승인
			newUser.permitSignup();
		}

		// 유저 단건 조회
		@Transactional(readOnly = true)
		public UserResponse getUser(Long userId) {
			// 유저 확인
			User user = checkUser.userCheck(userId);
			return new UserResponse(
					user.getId(),
					user.getEmail(),
					user.getUsername(),
					user.getPassword(),
					user.getRole().name(),
					user.getSlackId(),
					user.getStatus().name(),
					user.getHubId());
		}

		// 유저 다건 조회
		@Transactional(readOnly = true)
		public Page<UserResponse> getUsers(Long managerId, UserSearchRequest requestDto) {
			// 매니저 유저 확인
			User manager = checkUser.userCheck(managerId);
			UUID requestHubId = requestDto.hubId();
			// 허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
			if (requestHubId != null) {
				checkUser.checkHubManager(manager, requestHubId);
			} else {
				// 허브매니저인데 허브아이디가 null인 경우 자기 허브로 고정
				if (manager.getRole() == UserRole.HUB_MANAGER) requestHubId = manager.getHubId();
			}
			// 정렬조건 체크
			UserOrderBy.checkValid(requestDto.sortBy());

			// 오름차순/내림차순
			Sort.Direction directions =
					requestDto.sortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
			Pageable pageable =
					PageRequest.of(
							requestDto.page() - 1,
							requestDto.size(),
							Sort.by(directions, requestDto.sortBy()));
			Page<User> users =
					userRepository.findUsers(
							requestDto.username(),
							requestHubId,
							requestDto.role(),
							requestDto.status(),
							pageable);
			return users.map(user -> new UserResponse(
					user.getId(),
					user.getEmail(),
					user.getUsername(),
					user.getPassword(),
					user.getRole().name(),
					user.getSlackId(),
					user.getStatus().name(),
					user.getHubId()));
		}

		// 비밀번호 수정
		@Transactional
		public void updatePassword(Long userId, UpdatePasswordRequest requestDto) {
			// 유저 확인
			User user = checkUser.userCheck(userId);
			// 현재 비밀번호 확인
			if (!passwordEncoder.matches(requestDto.password(), user.getPassword()))
				throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHES);

			// 동일한 비밀번호인지 확인
			if (passwordEncoder.matches(requestDto.newPassword(), user.getPassword()))
				throw new CustomException(ErrorCode.PASSWORD_EQUAL);

			// 새 비밀번호 인코딩 및 업데이트
			user.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
		}

		// 슬랙 아이디 수정
		@Transactional
		public void updateSlack(Long userId, UpdateSlackRequest requestDto) {
			// 유저 확인
			User user = checkUser.userCheck(userId);
			// 슬랙 아이디 업데이트
			user.updateSlackId(requestDto.slackId());
		}

		@Transactional
		public void withdrawUser(Long userId) {
			// 유저 확인
			User user = checkUser.userCheck(userId);
			// 권한 정지
			user.updateReject();
		}

		//  회원 탈퇴 승인
		@Transactional
		public void permitDeleteUser(Long managerId, PermitRequest requestDto) {
			// 매니저 유저 확인
			User manager = checkUser.userCheck(managerId);
			// 승인 대상 유저 확인
			User user = checkUser.userCheck(requestDto.userId());

			// 허브 매니저라면 소속 허브 아이디 체크
			checkUser.checkHubManager(manager, user.getHubId());

			// 권한 정지 상태가 아니라면 예외 발생
			if (user.getStatus() != UserStatus.REJECTED) throw new CustomException(ErrorCode.NOT_REJECTED);
			// 탈퇴 신청한 유저 삭제
			user.delete();
		}

		public DeliveryManagerMessageResponse getDeliveryManagerMessageInfo(Long userId) {
			User user = checkUser.userCheck(userId);
			return new DeliveryManagerMessageResponse(user.getSlackId(), user.getUsername(), user.getEmail());
		}

		public UserHubIdResponse getUserHubInfo(Long userId) {
			User user = checkUser.userCheck(userId);
			return new UserHubIdResponse(user.getHubId());
		}
	}
}
