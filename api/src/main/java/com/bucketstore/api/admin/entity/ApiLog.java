package com.bucketstore.api.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;        // 호출된 API 경로
    private String method;     // GET, POST 등
    private String userId;     // 호출한 사용자 (비로그인 시 anonymous)
    private String status;        // HTTP 상태 코드
    private String failureReason; // 실패 사유 (에러 메시지)
    private LocalDateTime createdAt;

    @Builder
    public ApiLog(String url, String method, String userId, String status, String failureReason) {
        this.url = url;
        this.method = method;
        this.userId = userId;
        this.status = status;
        this.failureReason = failureReason;
        this.createdAt = LocalDateTime.now();
    }
}
