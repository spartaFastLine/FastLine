package com.fastline.authservice.application.service;

import com.fastline.authservice.application.change.DeliveryManagerService;
import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.authservice.presentation.dto.request.PermitRequest;
import com.fastline.authservice.presentation.dto.request.UpdatePasswordRequest;
import com.fastline.authservice.presentation.dto.request.UpdateSlackRequest;
import com.fastline.authservice.presentation.dto.request.UserSearchRequest;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerMessageResponse;
import com.fastline.authservice.presentation.dto.response.UserHubIdResponse;
import com.fastline.authservice.presentation.dto.response.UserResponse;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryManagerService.CheckUser checkUser;

    @Transactional
    public void permitSignup(Long managerId, Long userId) {
        User newUser = checkUser.userCheck(userId);
        // 매니저 유저 확인
        User manager = checkUser.userCheck(managerId);

        // 허브 매니저라면 소속 허브 아이디 체크
        checkUser.checkHubManager(manager, newUser.getHubId());
        // 승인 대기 상태가 아닌 경우 예외 발생
        if (newUser.getStatus() != UserStatus.PENDING) throw new CustomException(ErrorCode.NOT_PENDING);

        // 회원가입 승인
        newUser.permitSignup();
    }

    public Page<UserResponse> getUsers(Long userId, UserSearchRequest requestDto) {
        return null;
    }

    public UserResponse getUser(Long userId) {
        return null;
    }

    public void updatePassword(Long userId, @Valid UpdatePasswordRequest requestDto) {
    }

    public void updateSlack(Long userId, @Valid UpdateSlackRequest requestDto) {
    }

    public void withdrawUser(Long userId) {
    }

    public void permitDeleteUser(Long userId, @Valid PermitRequest requestDto) {
    }

    public DeliveryManagerMessageResponse getDeliveryManagerMessageInfo(Long userId) {
        return null;
    }

    public UserHubIdResponse getUserHubInfo(Long userId) {
        return null;
    }
}
