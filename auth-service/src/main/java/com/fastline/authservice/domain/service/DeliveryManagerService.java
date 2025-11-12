package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.*;
import com.fastline.authservice.domain.repository.DeliveryManagerRepository;
import com.fastline.authservice.presentation.request.DeliveryManagerCreateRequestDto;
import com.fastline.authservice.presentation.request.DeliveryManagerDeleteRequestDto;
import com.fastline.authservice.presentation.request.DeliveryManagerResponseDto;
import com.fastline.authservice.presentation.request.DeliveryManagerSearchRequestDto;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;
	private final CheckUser checkUser;

	// 배달 매니저 생성 - 마스터, 허브 관리자만 가능
	// todo : 사용자 승인시 동시 진행 - redis
	// todo  : requestDto의 배달 가능한 허브 아이디가 진짜 허브 아이디가 맞는지 확인
	@Transactional
	public void createDeliveryManager(Long managerId, DeliveryManagerCreateRequestDto requestDto) {
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.getUserId());
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
    //배달 매니저 생성 - 마스터, 허브 관리자만 가능
    //todo : 사용자 승인시 동시 진행
    //todo  : requestDto의 배달 가능한 허브 아이디가 진짜 허브 아이디가 맞는지 확인
    @Transactional
    public void createDeliveryManager(Long managerId, DeliveryManagerCreateRequestDto requestDto) {
        // 승인 대상 유저 확인
        User user = checkUser.userCheck(requestDto.getUserId());
        //매니저 유저 확인
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
				new DeliveryManager(user, DeliveryManagerType.valueOf(requestDto.getType()), number + 1);

		// 배달 매니저 저장
		deliveryManagerRepository.save(deliveryManager);
	}

	// 배달 매니저 단건 조회 - 배달 매니저 본인만 가능, 마스터나 허브 관리자는 다건 조회에서 가능
	@Transactional(readOnly = true)
	public DeliveryManagerResponseDto getDeliveryManager(Long userId) {
		// 정보 확인
		DeliveryManager manager = findDeliveryManager(userId);
		return new DeliveryManagerResponseDto(manager);
	}

	// 배달 매니저 검색
	public Page<DeliveryManagerResponseDto> getDeliveryManagers(
			Long managerId, @Valid DeliveryManagerSearchRequestDto requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		UUID hubId = requestDto.getHubId();
		// 허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
		if (hubId != null) checkUser.checkHubManager(manager, hubId);
		else {
			// 허브매니저인데 허브아이디가 null인 경우 자기 허브로 고정
			if (manager.getRole() == UserRole.HUB_MANAGER) hubId = manager.getHubId();
		}
		// 정렬조건 체크
		ManagerOrderBy.checkValid(requestDto.getSortBy());

		// 오름차순/내림차순
		Sort.Direction directions =
				requestDto.isSortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;

		Pageable pageable =
				PageRequest.of(
						requestDto.getPage() - 1,
						requestDto.getSize(),
						Sort.by(directions, requestDto.getSortBy()));

		Page<DeliveryManager> deliveryManagers =
				deliveryManagerRepository.findDeliveryManagers(
						requestDto.getUsername(),
						hubId,
						requestDto.getType(),
						requestDto.getNumber(),
						requestDto.getStatus(),
						requestDto.isActive(),
						pageable);
		return deliveryManagers.map(DeliveryManagerResponseDto::new);
	}

	@Transactional
	public void updateDeliveryManager(
			Long managerId, @Valid DeliveryManagerCreateRequestDto requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.getUserId());

		// 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
		checkUser.checkHubManager(manager, user.getHubId());

		// 회원이 배달매니저인지 확인
		checkDeliveryManager(user);

		DeliveryManager deliveryManager = findDeliveryManager(user.getId());
		if (deliveryManager.getDeletedAt() != null)
			throw new CustomException(ErrorCode.DELIVERY_MANAGER_DELETED);
		// 배달 매니저 타입 변경
		deliveryManager.updateType(DeliveryManagerType.valueOf(requestDto.getType()));
	}

	@Transactional
	public void deleteDeliveryManager(
			Long managerId, @Valid DeliveryManagerDeleteRequestDto requestDto) {
		// 매니저 유저 확인
		User manager = checkUser.userCheck(managerId);
		// 승인 대상 유저 확인
		User user = checkUser.userCheck(requestDto.getUserId());

		// 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
		checkUser.checkHubManager(manager, user.getHubId());

		// 배달 매니저 삭제
		DeliveryManager deliveryManager = findDeliveryManager(user.getId());
		deliveryManager.delete(manager.getId());
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
}
