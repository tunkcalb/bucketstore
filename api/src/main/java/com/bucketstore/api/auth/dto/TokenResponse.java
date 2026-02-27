package com.bucketstore.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    // 10분간 유효한 JWT 토큰 본문
    private String accessToken;

    // 토큰의 타입 (Bearer)
    private String tokenType;
}
