package com.fastline.authservice.application.change;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.authservice.presentation.dto.request.PermitRequest;
import com.fastline.authservice.presentation.dto.request.UpdateSlackRequest;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerMessageResponse;
import com.fastline.authservice.presentation.dto.response.UserHubIdResponse;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

//@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryManagerService.CheckUser checkUser;

    // 회원가입 승인

    // 유저 단건 조회

    // 유저 다건 조회

    // 비밀번호 수정

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
