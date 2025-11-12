package com.fastline.authservice.application.change;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.vo.UserOrderBy;
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
import com.fastline.common.security.model.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryManagerService.CheckUser checkUser;

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
