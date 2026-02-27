package com.bucketstore.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiLogResponse {
    private String url;        // 호출된 API 경로 (예: /order)
    private String method;     // HTTP 메서드 (예: POST, GET)
    private String userId;     // 호출한 사용자 ID (인증된 경우)
    private String status;        // HTTP 상태 코드
    private String failureReason; // 실패 사유 (에러 메시지)
    private LocalDateTime createdAt; // 호출 일시
}
