package com.bucketstore.api.admin.repository;

import com.bucketstore.api.admin.dto.ApiLogResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bucketstore.api.admin.entity.QApiLog.apiLog;

@RequiredArgsConstructor
public class ApiLogRepositoryImpl implements ApiLogRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public List<ApiLogResponse> getApiLogs() {
        return queryFactory
                .select(Projections.constructor(ApiLogResponse.class,
                        apiLog.url,
                        apiLog.method,
                        apiLog.userId,
                        apiLog.status,
                        apiLog.failureReason,
                        apiLog.createdAt
                ))
                .from(apiLog)
                .orderBy(apiLog.createdAt.desc())
                .fetch();
    }
}
