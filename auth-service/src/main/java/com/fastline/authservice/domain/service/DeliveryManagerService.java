package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.DeliveryManagerType;
import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.DeliveryManagerRepository;
import com.fastline.authservice.presentation.request.DeliveryManagerCreateRequestDto;
import com.fastline.authservice.presentation.request.DeliveryManagerResponseDto;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.security.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final CheckUser checkUser;

    //배달 매니저 생성 - 마스터, 허브 관리자만 가능
    //todo : 사용자 승인시 동시 진행 - redis
    //todo  : requestDto의 배달 가능한 허브 아이디가 진짜 허브 아이디가 맞는지 확인
    @Transactional
    public void createDeliveryManager(UserDetailsImpl manager, DeliveryManagerCreateRequestDto requestDto) {
        // 승인 대상 유저 확인
        User user = checkUser.userCheck(requestDto.getUserId());

        //이미 배달 매니저로 등록된 유저인지 확인
        if(deliveryManagerRepository.findById(user.getId()).isPresent()) throw new CustomException(ErrorCode.EXIST_DELIVERY_MANAGER);
        // 관리자가 MASTER가 아니거나 HUB_MANAGER인데 승인할 사용자가 다른 허브에 속해있다면 예외 발생
        checkUser.checkHubManager(manager, user.getHubId());
        // 회원 승인시 동시 진행
        if (user.getStatus()!= UserStatus.PENDING)
            throw new CustomException(ErrorCode.NOT_PENDING);

        //회원이 배달매니저인지 확인
        if(user.getRole() != UserRole.DELIVERY_MANAGER)throw new CustomException(ErrorCode.NOT_DELIVERY_MANAGER);


        //배송 순번은 현재 배달 매니저 수 +1
        long number = deliveryManagerRepository.countAll();

        // 배달 매니저 생성
        DeliveryManager deliveryManager = new DeliveryManager(user, DeliveryManagerType.valueOf(requestDto.getType()), number+1);

        //배달 매니저 저장
        deliveryManagerRepository.save(deliveryManager);
    }


    //배달 매니저 단건 조회 - 배달 매니저 본인만 가능, 마스터나 허브 관리자는 다건 조회에서 가능
    @Transactional(readOnly = true)
    public DeliveryManagerResponseDto getDeliveryManager(Long userId) {
        //정보 확인
        DeliveryManager manager = deliveryManagerRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));
        return new DeliveryManagerResponseDto(manager);
    }
}
