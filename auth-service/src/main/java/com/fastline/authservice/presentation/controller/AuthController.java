package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.security.UserDetailsImpl;
import com.fastline.authservice.domain.service.AuthService;
import com.fastline.authservice.presentation.request.LoginRequestDto;
import com.fastline.authservice.presentation.request.PermitRequestDto;
import com.fastline.authservice.presentation.request.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok().build();
    }

    //회원가입 승인
    @PutMapping("/permit/signup")
    public ResponseEntity permitSignup(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid PermitRequestDto requestDto) {
        authService.permitSignup(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.ok().build();
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse res) {
        authService.login(requestDto, res);
        return ResponseEntity.ok().build();
    }
}
