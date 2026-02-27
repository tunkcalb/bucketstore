package com.bucketstore.api.auth.service;

import com.bucketstore.api.auth.entity.Member;
import com.bucketstore.api.auth.repository.MemberRepository;
import com.bucketstore.api.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String loginOrRegister(String userId) {
        // 1. 유저가 없으면 새로 생성 (자동 회원가입)
        memberRepository.findByUserId(userId)
                .orElseGet(() -> memberRepository.save(new Member(userId)));

        // 2. JWT 발급 (10분 유효)
        return jwtTokenProvider.createToken(userId);
    }
}
