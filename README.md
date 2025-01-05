<div align="center">
  
## ✨싸그리싹싹(ssagri-ssakssak)✨

  
### ⛏ 기술 스택
![Java](https://img.shields.io/badge/Java-21-orange?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-green?logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-green?logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-green?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-5.7-blue?logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-ORM-orange)    
![Querydsl](https://img.shields.io/badge/Querydsl-blue?logo=hibernate&logoColor=white)

  
### 📌 서비스 개요
유저 계정의 해시태그(예: #dani)를 기반으로 인스타그램, 스레드, 페이스북, 트위터(X) 등<br>
다양한 SNS에 게시된 관련 게시물을 수집하고, 이를 한 곳에서 확인할 수 있는 **콘텐츠 통합 플랫폼**입니다.

### 🙋‍♀️ 팀 구성 및 역할

| 이름   | 역할              | GitHub 링크                      |
|--------|-------------------|----------------------------------|
| 박소미 | 로그인, JWT    | [GitHub](https://github.com/nyximos)   |
| 이소영 | 게시물 공유 및 좋아요       | [GitHub](https://github.com/0111s)   |
| 이혜지 | 게시물 목록 조회  | [GitHub](https://github.com/hyeji1020)   |
| 최하록 | 회원가입 및 게시물 통계 | [GitHub](https://github.com/dorianharok)   |


### ⏰ 개발 기간

2024.08.22 ~ 2024.08.30
  
</div>

## 📋 목차
1. [서비스 아키텍처](#1-서비스-아키텍처)
2. [구현 기능](#2-구현-기능)
3. [API 명세](#3-api-명세)
4. [ERD 및 디렉터리 구조](#4-erd-및-디렉터리-구조)


### 1. 서비스 아키텍처
추가 예정입니다.

### 2. 구현 기능
1. 회원가입 및 로그인
   - **회원가입**:
     - 이메일 및 비밀번호 유효성 검사.
     - 비밀번호는 **BCrypt**로 암호화 처리.
   - **로그인**:
     - `refreshToken`과 `accessToken`을 사용하여 인증 및 토큰 재발급.

2. 가입 승인
   - 유저 생성 시 **6자리 랜덤 코드**를 이메일로 발송.
   - 계정, 비밀번호, 인증코드 입력이 올바를 경우 **가입 승인 완료** 후 서비스 이용 가능.


3. 게시물 목록 조회
   - **API 기능**:
     - Feed에 표시될 게시물 목록을 조회.
   - **파라미터 설명**:

     | Parameter     | Type     | Default             | Description                                                                                  |
     |---------------|----------|---------------------|----------------------------------------------------------------------------------------------|
     | `hashtag`     | `string` | 본인 계정          | 정확히 일치하는 해시태그 검색. 예: `맛집`, `성수동`.                                         |
     | `type`        | `string` | 모든 타입 조회      | 게시물의 type별로 조회.                                                                      |
     | `order_by`    | `string` | `created_at`        | 정렬 기준: `created_at`, `updated_at`, `like_count`, `share_count`, `view_count`.             |
     | `search_by`   | `string` | `title,content`     | 검색 기준: 제목, 내용 또는 제목 + 내용 검색.                                                 |
     | `search`      | `string` |                     | 검색 키워드. 포함된 게시물 검색.                                                             |
     | `page_count`  | `number` | 10                  | 페이지당 게시물 수.                                                                          |
     | `page`        | `number` | 0                   | 조회하려는 페이지.                                                                           |

   - **기타 기능**:
     - `content`는 최대 **20자**까지만 표시.
     - 불필요한 필드 제외.


4. 게시물 상세 조회
   - 특정 게시물의 모든 필드 값 확인 가능.
   - API 호출 시 해당 게시물의 **조회수 증가**.


5. 게시물 좋아요
   - 좋아요 클릭 시 **외부 SNS API**를 호출하여 내부 서비스에서 좋아요 수 증가 처리.


6. 게시물 공유
   - 게시물은 외부 서비스에서 관리되며, 각 SNS 별 **공유 API 호출**을 통해 처리.


7. 게시물 통계 조회
   - **API 기능**:
     - 특정 해시태그의 게시물 통계를 제공.
   - **파라미터 설명**:

     | Parameter     | Type         | Default             | Description                                                                                  |
     |---------------|--------------|---------------------|----------------------------------------------------------------------------------------------|
     | `hashtag`     | `string`     | 본인 계정          | 조회할 해시태그.                                                                             |
     | `type`        | `enum`       | 필수 값            | `date` 또는 `hour`.                                                                         |
     | `start`       | `date`       | 오늘로부터 7일 전  | 조회 시작일 (예: `2023-10-01`).                                                             |
     | `end`         | `date`       | 오늘               | 조회 종료일 (예: `2023-10-25`).                                                             |
     | `value`       | `string`     | `count`            | `count`, `view_count`, `like_count`, `share_count` 중 하나.                                 |

   - **`value` 옵션**:
     - `count`: 게시물 개수.
     - `view_count`, `like_count`, `share_count`: 각 필드의 합계.

   - **통계 유형**:
     1. **`?value=count&type=date`**
        - `start` ~ `end` 기간 내(최대 30일) 해시태그가 포함된 게시물 수를 **일자별**로 제공.
     2. **`?value=count&type=hour`**
        - `start` ~ `end` 기간 내(최대 7일) 해시태그가 포함된 게시물 수를 **1시간 간격**으로 제공.

----

### 3. API 명세

| Method | 기능명       | URL                          | Auth Required |
|--------|--------------|------------------------------|---------------|
| POST   | 회원가입     | /auth/users                 | ✅             |
| PUT    | 가입승인     | /auth/users/approve         | ✅             |
| POST   | 토큰 재발급  | /auth/users/token/reissue   | ✅             |
| POST   | 로그인       | /auth/users/sign-in        | ✅             |
| GET    | 게시물 목록  | /boards                     | ✅             |
| GET    | 게시물 상세  | /boards/:id                 | ✅             |
| POST   | 게시물 좋아요 | /boards/:id/like           | ✅             |
| POST   | 게시물 공유  | /boards/:id/share          | ✅             |
| GET    | 통계         | /boards/stats              | ✅             |

### 4. ERD 및 디렉터리 구조

- ERD
  <br>
![image](https://github.com/user-attachments/assets/fb2d6a6b-31f7-4439-bb14-6934af4e3285)


<details>
<summary>디렉터리 구조 (🖱클릭)</summary>
  
  ```markdown
  ├── docs
  │   └── asciidoc
  │       ├── board
  │       │   └── board.adoc
  │       ├── index.adoc
  │       ├── token
  │       │   └── token.adoc
  │       └── user
  │           └── user.adoc
  ├── main
  │   ├── java
  │   │   └── com
  │   │       └── supernova
  │   │           └── ssagrissakssak
  │   │               ├── SsagriSsakssakApplication.java
  │   │               ├── client
  │   │               │   └── SnsApiClient.java
  │   │               ├── core
  │   │               │   ├── config
  │   │               │   │   ├── DefaultJpaRepository.java
  │   │               │   │   ├── HttpConfig.java
  │   │               │   │   ├── JpaConfig.java
  │   │               │   │   └── QueryDslConfig.java
  │   │               │   ├── constants
  │   │               │   │   ├── CommonConstant.java
  │   │               │   │   └── UserConstant.java
  │   │               │   ├── enums
  │   │               │   │   ├── ContentType.java
  │   │               │   │   ├── StatisticsTimeType.java
  │   │               │   │   └── StatisticsType.java
  │   │               │   ├── exception
  │   │               │   │   ├── BoardNotFoundException.java
  │   │               │   │   ├── ErrorCode.java
  │   │               │   │   ├── ExternalApiException.java
  │   │               │   │   ├── InvalidPasswordException.java
  │   │               │   │   ├── InvalidPeriodException.java
  │   │               │   │   ├── InvalidVerificationCodeException.java
  │   │               │   │   ├── JwtValidateException.java
  │   │               │   │   ├── SsagriException.java
  │   │               │   │   ├── UserNotFoundException.java
  │   │               │   │   └── UserRegistrationException.java
  │   │               │   ├── handler
  │   │               │   │   └── GlobalExceptionHandler.java
  │   │               │   ├── security
  │   │               │   │   ├── CustomAuthenticationEntryPoint.java
  │   │               │   │   ├── JwtProvider.java
  │   │               │   │   ├── JwtTokenFilter.java
  │   │               │   │   ├── LoginUser.java
  │   │               │   │   ├── SecurityConfig.java
  │   │               │   │   └── UserServiceImpl.java
  │   │               │   └── wrapper
  │   │               │       ├── PageResponse.java
  │   │               │       └── ResultResponse.java
  │   │               └── feed
  │   │                   ├── controller
  │   │                   │   ├── BoardController.java
  │   │                   │   ├── TokenController.java
  │   │                   │   ├── UserController.java
  │   │                   │   ├── request
  │   │                   │   │   ├── ApproveRequest.java
  │   │                   │   │   ├── BoardSearchRequest.java
  │   │                   │   │   ├── BoardStatisticsRequest.java
  │   │                   │   │   ├── SignInRequest.java
  │   │                   │   │   └── UserCreateRequest.java
  │   │                   │   └── response
  │   │                   │       ├── BoardDetailResponse.java
  │   │                   │       ├── BoardResponse.java
  │   │                   │       ├── DefaultIdResponse.java
  │   │                   │       ├── StatisticsResponse.java
  │   │                   │       └── TokenResponse.java
  │   │                   ├── persistence
  │   │                   │   └── repository
  │   │                   │       ├── BoardRepository.java
  │   │                   │       ├── HashtagRepository.java
  │   │                   │       ├── UserRepository.java
  │   │                   │       ├── custom
  │   │                   │       │   └── BoardRepositoryCustom.java
  │   │                   │       ├── entity
  │   │                   │       │   ├── BaseEntity.java
  │   │                   │       │   ├── BoardEntity.java
  │   │                   │       │   ├── HashtagEntity.java
  │   │                   │       │   └── UserEntity.java
  │   │                   │       ├── impl
  │   │                   │       │   └── BoardRepositoryImpl.java
  │   │                   │       └── model
  │   │                   │           └── StatisticsDto.java
  │   │                   └── service
  │   │                       ├── BoardService.java
  │   │                       ├── BoardStatisticsService.java
  │   │                       ├── TokenService.java
  │   │                       ├── UserService.java
  │   │                       └── delegator
  │   │                           ├── ApproveValidateDelegator.java
  │   │                           └── validator
  │   │                               ├── ActiveStatusValidator.java
  │   │                               ├── ApproveValidator.java
  │   │                               ├── PasswordValidator.java
  │   │                               ├── SignInValidator.java
  │   │                               ├── StatisticsValidator.java
  │   │                               └── VerificationCodeValidator.java
  │   └── resources
  │       └── application.yml
  └── test
      └── java
          └── com
              └── supernova
                  └── ssagrissakssak
                      ├── SsagriSsakssakApplicationTests.java
                      ├── client
                      │   └── SnsApiClientTest.java
                      ├── feed
                      │   ├── controller
                      │   │   ├── BoardControllerTest.java
                      │   │   ├── RestDocsSupport.java
                      │   │   ├── TokenControllerTest.java
                      │   │   ├── UserControllerTest.java
                      │   │   └── request
                      │   │       ├── ApproveRequestTest.java
                      │   │       ├── SignInRequestTest.java
                      │   │       └── UserCreateRequestTest.java
                      │   └── service
                      │       ├── BoardMockServiceTest.java
                      │       ├── BoardServiceTest.java
                      │       ├── BoardStatisticsServiceTest.java
                      │       ├── TokenServiceTest.java
                      │       ├── UserServiceTest.java
                      │       └── delegator
                      │           ├── ApproveValidateDelegatorTest.java
                      │           └── validator
                      │               ├── ActiveStatusValidatorTest.java
                      │               ├── PasswordValidatorTest.java
                      │               └── VerificationCodeValidatorTest.java
                      ├── fixture
                      │   ├── BoardFixture.java
                      │   ├── HashtagFixture.java
                      │   └── UserFixture.java
                      └── mockuser
                          ├── MockUser.java
                          └── WithMockCustomSecurityContextFactory.java

