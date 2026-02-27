package com.bucketstore.api.auth.controller;

import com.bucketstore.api.auth.dto.LoginRequest;
import com.bucketstore.api.auth.dto.TokenResponse;
import com.bucketstore.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * [토큰 발행 API]
     * user_id 입력 시 10분간 유효한 JWT 발급
     * DB에 해당 유저가 없으면 자동으로 회원가입 처리까지 진행
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = authService.loginOrRegister(request.getUserId());
        return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    }
}
