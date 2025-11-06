package com.fastline.authservice.domain.service;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserOrderBy;
import com.fastline.authservice.domain.model.UserRole;
import com.fastline.authservice.domain.repository.UserRepository;
import com.fastline.authservice.presentation.request.UpdatePasswordRequestDto;
import com.fastline.authservice.presentation.request.UpdateSlackRequestDto;
import com.fastline.authservice.presentation.request.UserResponseDto;
import com.fastline.authservice.presentation.request.UserSearchRequestDto;
import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //유저 단건 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        //유저 확인
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    //유저 다건 조회
    //todo : 정렬조건에 따라 정렬 로직 추가 필요
    //todo : 페이징 처리 추가 필요
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsers(Long userId, UserSearchRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        //허브가 null이 아닌데 해당 허브의 관리자가 아닌 경우 에러발생
        if(requestDto.getHubId()!=null) {
            if(user.getRole()!= UserRole.MASTER&&!user.getHubId().equals(requestDto.getHubId()))
                throw new CustomException(ErrorCode.NOT_HUB_MANAGER);
        }
        //정렬조건 체크
        UserOrderBy.checkValid(requestDto.getSortBy());

        //오름차순/내림차순
        Sort.Direction directions = requestDto.isSortAscending()? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(requestDto.getPage()-1, requestDto.getSize(), Sort.by(directions, requestDto.getSortBy()));
        Page<User> users = userRepository.findUsers(requestDto.getUsername(), requestDto.getHubId(), requestDto.getRole(), requestDto.getStatus(), pageable);
        return users.map(UserResponseDto::new);
    }

    //비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDto requestDto) {
        // 유저 확인
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 현재 비밀번호 확인
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHES);

        //동일한 비밀번호인지 확인
        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) throw new CustomException(ErrorCode.PASSWORD_EQUAL);

        // 새 비밀번호 인코딩 및 업데이트
        user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    //슬랙 아이디 수정
    @Transactional
    public void updateSlack(Long userId, UpdateSlackRequestDto requestDto) {
        // 유저 확인
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 슬랙 아이디 업데이트
        user.updateSlackId(requestDto.getSlackId());
    }


}
