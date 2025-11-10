package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.presentation.request.LoginRequestDto;
import com.fastline.authservice.presentation.request.SignupRequestDto;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.common.security.model.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public void signup(SignupRequestDto requestDto) {
		String email = requestDto.getEmail();
		String username = requestDto.getUsername();

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
		UserRole role = UserRole.valueOf(requestDto.getRoll());
		String password = passwordEncoder.encode(requestDto.getPassword());

		// 소속된 허브아이디 확인

		// 새로운 사용자 생성 및 저장
		User user =
				new User(email, username, password, role, requestDto.getHubId(), requestDto.getSlackId());
		userRepository.save(user);
	}


	public void login(@Valid LoginRequestDto requestDto, HttpServletResponse res) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

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
		String token = jwtUtil.createToken(user.getId(), username, user.getRole().toString(), user.getHubId(), user.getSlackId());
		res.setHeader("Authorization", token);
	}
}
