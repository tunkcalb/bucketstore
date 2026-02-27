package com.bucketstore.api.admin.repository;

import com.bucketstore.api.admin.dto.ApiLogResponse;

import java.util.List;

public interface ApiLogRepositoryCustom {
    public List<ApiLogResponse> getApiLogs();
}
