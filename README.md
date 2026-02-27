# 개발 환경 및 원칙

Language & Framework: Java 17 / Spring Boot 3.5.11


Database: MySQL 8.0 (JPA + QueryDSL 활용)


Infrastructure: Docker Compose 기반의 컨테이너 구동 환경


Auth: JWT (Stateless 인증)




1. 인증 및 보안 (JWT)

토큰 발행: user_id를 입력받아 10분간 유효한 JWT(JSON Web Token)를 발급합니다.


권한 관리: user_id가 superuser인 경우 ROLE_ADMIN 권한을 부여하고, 그 외 사용자는 ROLE_USER 권한을 가집니다.


인증 방식: 모든 API(인증 제외)는 헤더의 Authorization: Bearer {Token}을 통한 유효성 검증을 필수로 수행합니다.


보안 설정: Stateless 세션 정책을 채택하여 서버 부하를 줄이고, JwtAuthenticationFilter를 통해 필터 체인 레벨에서 보안을 강화했습니다.

2. 주문 및 동시성 제어


재고 경합 상황 처리: 다수의 사용자가 동시에 동일 상품을 주문할 때 발생하는 재고 부족 및 부정합을 방지하기 위해 비관적 락(Pessimistic Lock)을 활용하여 원자성을 보장했습니다.


원자성 보장: 주문 상품 중 하나라도 재고가 부족하면 전체 주문이 확정되지 않도록 트랜잭션을 관리합니다.

3. 어드민 모니터링 시스템

전역 API 로깅: HandlerInterceptor를 통해 모든 API 호출의 성공/실패 사유, 요청자, 일시를 기록하여 보안 감사 로그로 활용합니다.

데이터 정합성 고도화:

전량 취소된 주문은 제외하고, 유효한 최신 주문 일시만 산출하도록 쿼리를 최적화했습니다.

- GET /admin/logs: 전역 API 호출 이력 조회 (성공/실패 사유 포함)

- GET /admin/inventory: 실시간 재고 및 상품 상태 모니터링


# 컨테이너 빌드 및 실행
docker-compose up --build -d

초기 데이터: ./src/main/resources/init.sql을 통해 과제 가이드라인에 명시된 상품 데이터(11101JS505 등)가 자동으로 적재됩니다.

Application Port: 8080

MySQL Port: 3306

# 구현 완료 항목
✅ JWT 기반 인증 및 superuser 권한 관리

✅ 주문 생성 시 동시성 제어 및 원자성 확보

✅ 주문 부분 취소 및 실시간 재고 복원

✅ 최근 주문 순 정렬 및 5개 단위 페이징 조회

✅ 어드민 API: 호출 이력 조회 및 재고 모니터링

✅ 주문 API 단위/통합 테스트 코드 작성