# Gongspot - SpringBoot

### 🪄 공스팟 서버 레포지토리입니다.


### 👨‍👩‍👧‍👦 팀원 소개

| ![](https://imgur.com/QqNCF76.png) | ![](https://imgur.com/Dfl835f.png)| ![](https://imgur.com/W35htQN.png) |   ![](https://imgur.com/aP69LSC.png) | ![](https://imgur.com/vjlGerW.png) |
| :--: | :--: | :--: | :--: | :--: |
| **이윤지** | **홍지호** | **정준영** | **곽재현** | **조은정** |



### 🦾 프로젝트 소개

`Gongspot`은 청년들의 공부 공간 정보(혼잡도, 리뷰)등을 공유할 수 있는 서비스입니다. **SpringBoot**를 기반으로 개발되었으며, RESTful API를 통해 클라이언트와 통신합니다.

-----

### 🛠 기술 스택

**프레임워크**
- Spring Boot
- Spring Security (OAuth2, JWT 기반 인증/인가)

**보안 및 인증**
- OAuth2 (카카오 로그인 연동)
- JWT (Access Token + Refresh Token)
- Redis (블랙리스트 저장 및 토큰 관리)

**데이터베이스**
- MySQL 8.x
- Hibernate

**API 문서화**
- Swagger (springdoc-openapi)

**빌드 도구**
- Gradle

**배포 및 CI/CD**
- GitHub Actions (배포 자동화)
- AWS EC2 (Ubuntu 서버 직접 배포)
- NGINX (리버스 프록시 + HTTPS)

-----

### 🏗️ 프로젝트 구조

<img width="2539" height="1011" alt="Frame 3" src="https://github.com/user-attachments/assets/c64d942f-e282-4e0e-bf46-688290cf44f2" />


**도메인 주도 설계(DDD, Domain-Driven Design)** 와 **관심사 분리(Separation of Concerns)** 원칙을 기반으로 구조화하였습니다.

핵심 도메인별로 entity, service, controller, repository를 나누어 유지보수성과 확장성을 높였으며, 공통 기능들은 common과 global 패키지에 집중시켜 중복 로직을 방지하고 재사용성을 높였습니다.

#### `com.gongspot.project.common`

**code**
- **status**: 공통 응답 코드, 상태 코드 관리
- `ErrorStatus`, `SuccessStatus`: 에러 및 성공 상태를 enum으로 정의
- `BaseCode`, `BaseErrorCode`: 공통 코드 베이스 인터페이스
- `ErrorReasonDTO`, `ReasonDTO`: 에러 응답 상세 메시지 전달용 DTO

**entity**
- `BaseEntity`: 모든 엔티티에 상속되는 공통 필드 (createdAt, updatedAt, deletedAt) 관리

**enums**
- 전체 도메인에서 사용하는 공통 enum 모음
- 글로벌 예외 처리 및 공통 에러 관리
- `BaseHandler`, `ExceptionAdvice`, `GeneralException`: 통합된 예외 흐름 처리 및 커스텀 에러 메시지 설정

**response**
- `ApiResponse`: 응답 통일화 및 일관성을 위한 표준 응답 클래스

#### `com.gongspot.project.domain`

**핵심 비즈니스 로직별 도메인 디렉토리**

**alarm/entity ~ search/entity**
- 각 폴더는 개별 도메인 (알림, 배너, 좋아요, 미디어, 신규 장소, 알림, 장소, 포인트, 리뷰, 검색, 유저)에 대응
- `entity` 디렉토리 안에는 해당 도메인의 Entity 클래스만 관리
- 이후 필요 시 service, controller, repository가 동일한 도메인 디렉토리에 추가될 수 있도록 설계됨

**예시**
- `alarm/entity`: Alarm, AlarmType 엔티티
- `place/entity`: Place, Place 관련 컬렉션 테이블
- `review/entity`: Review, ReviewMedia 등

#### `com.gongspot.project.global`

**config**
- 글로벌 설정 파일 관리
- `QueryDSLConfig`: QueryDSL 설정
- `SwaggerConfig`: Swagger 문서화 설정
- 추후 글로벌 시큐리티, 글로벌 필터, 글로벌 메시지 컨버터 등 추가 가능

#### test
- 테스트용 컨트롤러와 서비스 클래스 예제

-----

### 🌿 Git-flow 전략

**Git-flow Strategy**를 채택하여 체계적인 버전 관리와 협업 환경을 구축했습니다.

- **`main`**: 최종적으로 사용자에게 배포되는 가장 안정적인 버전 브랜치
- **`develop`**: 다음 출시 버전을 개발하는 중심 브랜치. 기능 개발 완료 후 `feature` 브랜치들이 병합
- **`feature`**: 기능 개발용 브랜치. `develop`에서 분기하여 작업
- **`refactor`**: 코드 리팩토링 전용 브랜치. 기능 변경 없이 코드 구조 개선 작업
- **`hotfix`**: 운영 환경 긴급 수정용 브랜치. `main`에서 분기하여 즉시 수정 필요한 버그 해결

### 🚧 API 문서

API 문서는 Swagger를 통해 제공됩니다. 아래 URL로 접속하면 확인할 수 있습니다.
`https://api.gongspot.site/swagger-ui/index.html#/`

