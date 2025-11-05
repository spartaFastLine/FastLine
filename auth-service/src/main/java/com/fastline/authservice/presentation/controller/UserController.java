package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.security.UserDetailsImpl;
import com.fastline.authservice.domain.service.UserService;
import com.fastline.authservice.presentation.request.UpdatePasswordRequestDto;
import com.fastline.authservice.presentation.request.UpdateSlackRequestDto;
import com.fastline.authservice.presentation.request.UserResponseDto;
import com.fastline.authservice.presentation.request.UserSearchRequestDto;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //유저 다건 조회
    @GetMapping("/managers/users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserSearchRequestDto requestDto) {
        List<UserResponseDto> responseDto = userService.getUsers(userDetails.getUser().getId(), requestDto);
        return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);

    }
    //유저 단건 조회- 전체 가능
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto responseDto = userService.getUser(userDetails.getUser().getId());
        return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);
    }
    //비밀번호 수정- 전체 가능
    @PutMapping("/user/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UpdatePasswordRequestDto requestDto) {
        userService.updatePassword(userDetails.getUser().getId(), requestDto);
        return ResponseUtil.successResponse(SuccessCode.PASSWORD_UPDATE_SUCCESS);
    }
    //슬랙 아이디 수정- 전체 가능
    @PutMapping("/user/slack")
    public ResponseEntity<ApiResponse<Void>> updateUserSlack(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UpdateSlackRequestDto requestDto) {
        userService.updateSlack(userDetails.getUser().getId(), requestDto);
        return ResponseUtil.successResponse(SuccessCode.USER_UPDATE_SUCCESS);
    }
    //회원 탈퇴 신청
    //회원 탈퇴 승인
}
