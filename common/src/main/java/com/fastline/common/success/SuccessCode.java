package com.fastline.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* 필요 시 성공코드 추가 후 사용 */
@Getter
@RequiredArgsConstructor
public enum SuccessCode {

	// 회원 (user)
	USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공, 승인을 기다려주세요."),
	USER_SIGNUP_PERMIT_SUCCESS(HttpStatus.CREATED, "회원가입 승인 성공"),
	USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	USER_READ_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
	USER_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정 성공"),
	PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "비밀번호 수정 성공"),
	USER_WITHDRAWAL_REQUEST_SUCCESS(HttpStatus.OK, "회원탈퇴 신청 성공"),
	USER_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "회원 삭제 성공"),
	SLACKID_READ_SUCCESS(HttpStatus.OK, "사용자 슬랙 아이디 조회 성공"),
	HUBID_READ_SUCCESS(HttpStatus.OK, "사용자 허브 아이디 조회 성공"),
	DELIVERY_MANAGER_ASSIGN_SUCCESS(HttpStatus.OK, "배송 담당자 자동 배정 성공"),
	DELIVERY_MANAGER_COMPLETE_SUCCESS(HttpStatus.OK, "배송 담당자 완료 처리 성공"),

	// 배달 매니저(delivery manager)
	DELIVERY_MANAGER_CREATE_SUCCESS(HttpStatus.CREATED, "배달 매니저 생성 성공"),
	DELIVERY_MANAGER_READ_SUCCESS(HttpStatus.OK, "배달 매니저 조회 성공"),
	DELIVERY_MANAGER_UPDATE_SUCCESS(HttpStatus.CREATED, "배달 매니저 수정 성공"),
	DELIVERY_MANAGER_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "배달 매니저 삭제 성공"),

	// 업체 (vendor)
	VENDOR_SAVE_SUCCESS(HttpStatus.CREATED, "업체 등록 성공"),
	VENDOR_FIND_SUCCESS(HttpStatus.OK, "업체 조회 성공"),
	VENDOR_UPDATE_SUCCESS(HttpStatus.OK, "업체 정보 수정 성공"),
	VENDOR_DELETE_SUCCESS(HttpStatus.OK, "업체 삭제 성공"),
	PRODUCT_SAVE_SUCCESS(HttpStatus.CREATED, "상품 등록 성공"),
	PRODUCT_FIND_SUCCESS(HttpStatus.OK, "상품 조회 성공"),
	PRODUCT_UPDATE_SUCCESS(HttpStatus.OK, "상품 정보 수정 성공"),
	PRODUCT_DELETE_SUCCESS(HttpStatus.OK, "상품 삭제 성공"),
	ORDER_SAVE_SUCCESS(HttpStatus.OK, "주문 등록 성공"),
	ORDER_FIND_SUCCESS(HttpStatus.OK, "주문 조회 성공"),
	ORDER_STATUS_UPDATE_SUCCESS(HttpStatus.OK, "주문 상태 변경 성공"),
	ORDER_DELETE_SUCCESS(HttpStatus.OK, "주문 삭제 성공"),

	// AI
	MESSAGE_GENERATION_SUCCESS(HttpStatus.OK, "최종 발송 시한 정보 생성 성공"),

	// 메세지 (messaging)
	SLACK_MESSAGE_SENT_SUCCESS(HttpStatus.OK, "슬랙 메세지 전송 성공"),

	// 배송 (delivery)
	DELIVERY_SAVE_SUCCESS(HttpStatus.CREATED, "배송 생성 성공"),
	DELIVERY_FROM_ORDER_SAVE_SUCCESS(HttpStatus.CREATED, "주문 생성 시 배송 생성 성공"),
	DELIVERY_FIND_SUCCESS(HttpStatus.OK, "배송 단건 조회 성공"),
	DELIVERY_SEARCH_SUCCESS(HttpStatus.OK, "배송 검색 성공"),
	DELIVERY_UPDATE_SUCCESS(HttpStatus.OK, "배송 수정 성공"),
	DELIVERY_DELETE_SUCCESS(HttpStatus.OK, "배송 삭제 성공"),
	DELIVERY_PATH_DELETE_SUCCESS(HttpStatus.OK, "배송 경로 기록 삭제 성공"),
	DELIVERY_STATUS_UPDATE_SUCCESS(HttpStatus.OK, "배송 상태 변경 성공"),
	DELIVERY_COMPLETE_SUCCESS(HttpStatus.OK, "배송 완료 처리 성공"),
    DELIVERY_PATHS_FIND_SUCCESS(HttpStatus.OK, "배송별 경로 전체 조회"),
    DELIVERY_PATH_FIND_SUCCESS(HttpStatus.OK, "단일 경로 기록 조회"),
    DELIVERY_PATH_SEARCH_SUCCESS(HttpStatus.OK, "배송 경로 검색 성공");

	private final HttpStatus httpStatus;
	private final String message;
}
