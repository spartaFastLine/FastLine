package com.fastline.authservice.domain.security;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//DB의 회원정보 조회 -> Spring Security의 인증 관리자(UserDetials)에게 전달
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    //pk는 userId인데 바꿀 수 없는지
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        if(user.getStatus()!= UserStatus.APPROVE) throw new IllegalArgumentException("승인된 사용자가 아닙니다.");
        return new UserDetailsImpl(user);
    }
}
