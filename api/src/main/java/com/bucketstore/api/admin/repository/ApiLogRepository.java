package com.bucketstore.api.admin.repository;

import com.bucketstore.api.admin.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> , ApiLogRepositoryCustom {
}
