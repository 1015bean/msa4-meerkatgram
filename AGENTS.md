# Meerkatgram 프로젝트 구조 및 레이어 역할 정의

이 문서는 `msa4-meerkatgram` 프로젝트의 전체 디렉토리 구조와 각 패키지/레이어가 담당하는 역할에 대해 설명합니다.

---

## 1. 전체 디렉토리 구조 (Directory Structure)

```text
msa4-meerkatgram/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── msa4meerkatgram/
│   │   │           ├── Msa4MeerkatgramApplication.java  (애플리케이션 메인 진입점)
│   │   │           ├── domain/                         (비즈니스 도메인 레이어)
│   │   │           │   ├── auth/                       (인증 및 인가 도메인)
│   │   │           │   ├── file/                       (파일 업로드 및 처리 도메인)
│   │   │           │   ├── post/                       (피드/게시글 도메인)
│   │   │           │   └── user/                       (사용자/회원 도메인)
│   │   │           └── global/                         (공통 인프라 및 유틸리티 레이어)
│   │   │               ├── config/                     (설정 클래스)
│   │   │               ├── errors/                     (예외 처리)
│   │   │               ├── responses/                  (공통 API 응답 정의)
│   │   │               ├── security/                   (보안 및 JWT 설정)
│   │   │               └── util/                       (공통 유틸리티)
│   │   └── resources/                                  (설정 및 자원 파일)
│   │       ├── application.yaml                        (기본 환경 설정)
│   │       ├── application-exam.yaml                   (샘플 설정 파일)
│   │       └── dummy/                                  (더미 데이터 파일 등)
│   └── test/                                           (테스트 코드)
├── build.gradle                                        (Gradle 빌드 설정 및 의존성 관리)
├── settings.gradle                                     (Gradle 프로젝트 명명 설정)
├── README.md                                           (프로젝트 소개 문서)
└── AGENTS.md                                           (본 구조 정의 문서)
```

---

## 2. 레이어별 역할 및 책임 (Layer Roles & Responsibilities)

### 2.1. 도메인 레이어 ([domain](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/domain))
비즈니스 로직과 서비스의 핵심 기능들을 담당하며, 각 도메인 단위로 격리되어 관리됩니다.

*   **`controllers` (컨트롤러 레이어)**
    *   **역할**: 클라이언트의 HTTP 요청을 수신하고 비즈니스 서비스로 요청을 분기하며, 최종 결과를 HTTP 응답 형식으로 변환하여 전달합니다.
    *   **세부 내용**: 요청 파라미터나 Body의 유효성 검증(Validation)을 수행하고, 적절한 DTO(`requests`, `responses`)를 활용합니다.
*   **`services` (서비스 레이어)**
    *   **역할**: 애플리케이션의 핵심 비즈니스 로직을 구현합니다.
    *   **세부 내용**: 컨트롤러로부터 받은 데이터를 가공하여 필요한 비즈니스 규칙을 수행하고, 데이터 접근을 위해 리포지토리를 호출합니다. 트랜잭션 경계(`@Transactional`)가 선언되는 계층입니다.
*   **`repositories` (데이터 접근 레이어)**
    *   **역할**: 데이터베이스나 다른 영속성 저장소와의 상호작용을 처리합니다.
    *   **세부 내용**: Spring Data JPA 인터페이스 및 QueryDSL 등을 사용하여 데이터의 CRUD 및 복잡한 조건 조회를 담당합니다.
*   **`entities` (엔티티 레이어)**
    *   **역할**: 실제 데이터베이스 테이블과 1:1로 매핑되는 도메인 모델 클래스들입니다.
    *   **세부 내용**: JPA 애노테이션을 가지며, 도메인의 상태 정보와 비즈니스 핵심 규칙(핵심 비즈니스 메서드)을 내포할 수 있습니다.
*   **`requests` / `responses` (DTO 레이어)**
    *   **역할**: 클라이언트와 서버 간의 통신(HTTP 요청/응답) 시 주고받는 데이터의 포맷을 정의합니다.
    *   **세부 내용**: 엔티티 객체의 내부 구조가 외부에 직접 노출되는 것을 방지하고, 전송 데이터의 최소화 및 정제를 위해 사용됩니다.

---

### 2.2. 전역 인프라 레이어 ([global](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/global))
도메인에 종속되지 않고, 애플리케이션 전체에 걸쳐 공통적으로 적용되는 설정을 담당합니다.

*   **`config` (설정 계층)**
    *   CORS 설정 ([CorsConfig.java](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/global/config/CorsConfig.java)), WebMvc 설정 ([WebConfig.java](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/global/config/WebConfig.java)) 및 JPA Auditing 등 전역적인 설정을 관리합니다.
*   **`errors` (예외 처리 계층)**
    *   애플리케이션 전역에서 발생하는 예외를 잡아서 규격화된 에러 응답을 반환하는 컨트롤러 어드바이스([GlobalExceptionHandler.java](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/global/errors/GlobalExceptionHandler.java)) 및 비즈니스 특화 커스텀 예외들을 관리합니다.
*   **`responses` (공통 응답 계층)**
    *   일관성 있는 API 인터페이스를 제공하기 위해 전역 공통 응답 규격 클래스([GlobalRes.java](file:///E:/siro/workspace/msa4-meerkatgram/src/main/java/com/msa4meerkatgram/global/responses/GlobalRes.java))를 관리합니다.
*   **`security` (보안 및 JWT 계층)**
    *   Spring Security 설정, JWT 발행/검증 필터, 쿠키 제어 유틸리티 등 사용자 인증 및 권한 검증에 필요한 필터와 유틸리티 클래스들이 배치됩니다.
*   **`util` (공통 유틸리티 계층)**
    *   파일 입출력 처리 등 도메인에 얽매이지 않고 애플리케이션 전반에서 사용할 수 있는 순수 헬퍼 기능을 정의합니다.

---

## 3. 핵심 설정 파일 정보

*   [build.gradle](file:///E:/siro/workspace/msa4-meerkatgram/build.gradle): 프로젝트의 의존성(Spring Boot Starter Web, JPA, Security, Lombok 등) 및 빌드 환경이 선언되어 있습니다.
*   [application.yaml](file:///E:/siro/workspace/msa4-meerkatgram/src/main/resources/application.yaml): 데이터베이스 커넥션 풀 정보, JWT 키 값, JPA 쿼리 출력 로그 여부 등 애플리케이션 가동에 필요한 핵심 매개변수들이 기록되어 있습니다.
