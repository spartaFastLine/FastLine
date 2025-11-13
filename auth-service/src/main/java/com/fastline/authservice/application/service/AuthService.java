package com.fastline.authservice.application.service;

import com.fastline.authservice.application.command.LoginCommand;
import com.fastline.authservice.application.command.SignupCommand;
import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.vo.DeliveryManagerType;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.common.security.model.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final HubClient hubClient;

	// todo : 허브여부체크 다시 풀기
	public void signup(SignupCommand command) {
		String email = command.email();
		String username = command.username();
		UUID hubId = command.hubId();

		// 회원 중복 확인 - 코드 다시
		userRepository
				.findUserByEmail(email)
				.ifPresent(
						user -> {
							throw new CustomException(ErrorCode.EXIST_EMAIL);
						});
		// username 중복 확인
		userRepository
				.findUserByUsername(username)
				.ifPresent(
						user1 -> {
							throw new CustomException(ErrorCode.EXIST_USERNAME);
						});

		String password = passwordEncoder.encode(command.password());

		// 소속된 허브아이디 확인
		//        hubClient.getHubExists(hubId);

		// 사용자 ROLE 확인
		UserRole role = UserRole.valueOf(command.roll());
		// 새로운 사용자 생성 및 저장
		User user;
		if (role != UserRole.DELIVERY_MANAGER) {
			user = new User(email, username, password, role, hubId, command.slackId());
		} else {
			Long count = userRepository.countDeliveryManagers();
			if (!StringUtils.hasText(command.deliveryType()))
				throw new CustomException(ErrorCode.INVALID_DELIVERY_MANAGER_TYPE);

			DeliveryManagerType deliveryType = DeliveryManagerType.valueOf(command.deliveryType());
			user =
					new User(
							email, username, password, role, hubId, command.slackId(), deliveryType, count + 1);
		}
		userRepository.save(user);
	}

	public void login(LoginCommand command, HttpServletResponse res) {
		String username = command.username();
		String password = command.password();

		User user =
				userRepository
						.findUserByUsername(username)
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
