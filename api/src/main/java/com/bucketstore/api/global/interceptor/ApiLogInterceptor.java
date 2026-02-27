package com.bucketstore.api.global.interceptor;

import com.bucketstore.api.admin.entity.ApiLog;
import com.bucketstore.api.admin.repository.ApiLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ApiLogInterceptor implements HandlerInterceptor {

    private final ApiLogRepository apiLogRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // /error 경로는 로그에서 제외 (Spring Boot 내부 오류 처리용)
        if (request.getRequestURI().equals("/error")) {
            return;
        }

        // 1. 보안 컨텍스트에서 사용자 확인
        String userId = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous";

        // 2. HTTP 상태 코드 및 예외 메시지 가져오기
        String status = (ex == null) ? "SUCCESS" : "FAIL";
        String failureReason = (ex != null) ? ex.getMessage() : null;

        // 3. 로그 저장
        ApiLog log = ApiLog.builder()
                .url(request.getRequestURI())
                .method(request.getMethod())
                .userId(userId)
                .status(status)
                .failureReason(failureReason)
                .build();

        apiLogRepository.save(log);
    }
}
