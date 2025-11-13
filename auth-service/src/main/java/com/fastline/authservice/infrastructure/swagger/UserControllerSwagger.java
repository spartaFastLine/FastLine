package com.fastline.authservice.infrastructure.swagger;

import com.fastline.authservice.presentation.dto.request.*;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerResponse;
import com.fastline.authservice.presentation.dto.response.UserResponse;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.security.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="사용자 관리 API", description = "사용자 관련 API")
@SecurityRequirement(name = "bearerAuth")
public interface UserControllerSwagger {


    @Operation(summary = "회원가입 승인", description = "관리자가 사용자의 회원가입을 승인합니다.\n MASTER 또는 사용자가 소속된 HUB_MANAGER만이 가능합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
    })
    @PutMapping("/permit/signup/{userId}")
    ResponseEntity<ApiResponse<Void>> permitSignup(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("userId") Long userId);



    @Operation(summary = "사용자 정보 검색", description = "MASTER 또는 사용자가 소속된 HUB_MANAGER만이 가능하며 검색 조건을 기반으로 사용자 정보를 조회합니다.\n 허브 관리자의 경우 자동으로 소속된 허브로 검색이 제한됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
    })
    @GetMapping("/managers/users")
    ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody UserSearchRequest requestDto);



    @Operation(summary = "배송매니저 정보 검색", description = "MASTER 또는 사용자가 소속된 HUB_MANAGER만이 가능하며 검색 조건을 기반으로 사용자 정보를 조회합니다.\n 허브 관리자의 경우 자동으로 소속된 허브로 검색이 제한됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @GetMapping("/managers")
    ResponseEntity<ApiResponse<Page<DeliveryManagerResponse>>> getDeliveryManagers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid DeliveryManagerSearchRequest request);



    @Operation(summary = "사용자 정보 조회", description = "본인의 사용자 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/user")
    ResponseEntity<ApiResponse<UserResponse>> getUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails);



    @Operation(summary = "비밀번호 변경", description = "본인의 비밀번호를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping("/user/password")
    ResponseEntity<ApiResponse<Void>> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid UpdatePasswordRequest requestDto);



    @Operation(summary = "슬랙 아이디 변경", description = "본인의 슬랙 아이디를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping("/user/slack")
    ResponseEntity<ApiResponse<Void>> updateUserSlack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid UpdateSlackRequest requestDto);


    @Operation(summary = "사용자 정보 변경", description = "MASTER 또는 사용자가 소속된 HUB_MANAGER만이 가능하며 해당 유저아이디를 가진 사용자 정보를 수정합니다.\n 관리자는 강제로 권한을 활성화/정지/탈퇴시킬 수 있습니다\n 소속된 허브를 변경하는 요청은 변경전 허브의 관리자만 가능합니다\n 변경 대상이 배송매니저인 경우 허브/업체 배송 타입을 변경할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PutMapping("/{userId}")
    ResponseEntity<ApiResponse<Void>> updateDeliveryManager(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserManagerUpdateRequest request);


    @Operation(summary = "회원탈퇴 신청", description = "로그인한 회원이 탈퇴를 신청합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/user/withdraw")
    ResponseEntity<ApiResponse<Void>> withdrawUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails);


    @Operation(summary = "회원 탈퇴 승인", description = "MASTER 또는 사용자가 소속된 HUB_MANAGER만이 가능하며 해당 유저아이디의 탈퇴를 승인합니다.\n 탈퇴를 신청한 사용자만 탈퇴 승인이 가능합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @DeleteMapping("/managers/withdraw/{userId}/permit")
    ResponseEntity<ApiResponse<Void>> deleteUserpermit(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("userId") Long userId);
}
