package com.bucketstore.api.admin.controller;


import com.bucketstore.api.admin.dto.ApiLogResponse;
import com.bucketstore.api.admin.dto.ProductInventoryResponse;
import com.bucketstore.api.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * [재고 모니터링 API]
     * 모든 상품의 재고 현황과 마지막 유효 주문 일시를 확인합니다.
     */
    @GetMapping("/inventory")
    public ResponseEntity<List<ProductInventoryResponse>> getInventoryStatus() {
        return ResponseEntity.ok(adminService.getInventoryStatus());
    }

    /**
     * [API 호출 로그 조회]
     * 과제 요구사항: API 호출 로그(URL, 일시 등)를 확인합니다.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<ApiLogResponse>> getApiLogs() {
        return ResponseEntity.ok(adminService.getApiLogs());
    }
}
