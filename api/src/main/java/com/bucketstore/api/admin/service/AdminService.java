package com.bucketstore.api.admin.service;

import com.bucketstore.api.admin.dto.ApiLogResponse;
import com.bucketstore.api.admin.dto.ProductInventoryResponse;
import com.bucketstore.api.admin.repository.AdminRepository;
import com.bucketstore.api.admin.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final ApiLogRepository apiLogRepository;

    public List<ProductInventoryResponse> getInventoryStatus() {
        return adminRepository.getProductInventoryStatus();
    }

    public List<ApiLogResponse> getApiLogs() {
        return apiLogRepository.getApiLogs();
    }
}
