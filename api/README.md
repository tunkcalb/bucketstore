1. 인증 및 보안 (JWT)

토큰 발행: user_id를 입력받아 10분간 유효한 JWT(JSON Web Token)를 발급합니다.


권한 관리: user_id가 superuser인 경우 ROLE_ADMIN 권한을 부여하고, 그 외 사용자는 ROLE_USER 권한을 가집니다.


인증 방식: 모든 API(인증 제외)는 헤더의 Authorization: Bearer {Token}을 통한 유효성 검증을 필수로 수행합니다.


보안 설정: Stateless 세션 정책을 채택하여 서버 부하를 줄이고, JwtAuthenticationFilter를 통해 필터 체인 레벨에서 보안을 강화했습니다.