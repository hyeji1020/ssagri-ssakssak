<div align="center">
  
## ✨싸그리싹싹(ssagri-ssakssak)✨

  
### ⛏ 기술 스택
![Java](https://img.shields.io/badge/Java-21-orange?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green?logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-green?logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-green?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-5.7-blue?logo=mysql&logoColor=white)
![Querydsl](https://img.shields.io/badge/Querydsl-blue?logo=hibernate&logoColor=white)

  
### 📌 서비스 개요
유저 계정의 해시태그(”#dani”) 를 기반으로 인스타그램, 스레드, 페이스북, 트위터(X) 등<br>
복수의 SNS에 게시된 게시물 중 유저의 해시태그가 포함된 게시물들을 하나의 서비스에서 확인할 수 있는 **통합 Feed 어플리케이션** 입니다.

### 🙋‍♀️ 팀 구성 및 역할

| 이름   | 역할              | GitHub 링크                      |
|--------|-------------------|----------------------------------|
| 박소미 | 로그인    | [GitHub](https://github.com/nyximos)   |
| 이소영 | 게시물 공유 및 좋아요       | [GitHub](https://github.com/0111s)   |
| 이혜지 | 게시물 목록 조회  | [GitHub](https://github.com/hyeji1020)   |
| 최하록 | 회원가입 및 게시물 통계 | [GitHub](https://github.com/dorianharok)   |


### ⏰ 개발 기간

2024.08.22 ~ 2024.08.30
  
</div>

### 📋 목차
1. [서비스 아키텍처](#서비스-아키텍처)
2. [구현 기능](#구현-기능)
3. [API 명세](#api-명세)
4. [ERD 및 디렉터리 구조](#erd-및-디렉터리-구조)


### 1. 서비스 아키텍처
추가 예정입니다.

### 2. 구현 기능
  
  - 회원가입 및 로그인
    - 이메일 및 비밀번호 유효성 검사.
    - 비밀번호는 `BCrypt`로 암호화 처리.
    - `refreshToken`을 사용하여 `accessToken`과 `refreshToken`을 재발급.
  
  - 가입 승인
    - 유저 생성 시 **6자리 랜덤 코드**를 입력한 이메일로 발송.
    - 계정, 비밀번호, 인증코드가 올바르게 입력되었을 경우 **가입 승인**이 완료되어 서비스 이용 가능.
    
  - 게시물 목록
    - Feed에 표시될 게시물 목록을 조회하는 API.
    - <details>
      <summary> 다음 쿼리 파라미터를 지원:</summary>
        
      | Parameter   | Type     | Default             | Description                                                                                  |
      |-------------|----------|---------------------|----------------------------------------------------------------------------------------------|
      | `hashtag`   | `string` | 본인 계정          | 정확히 일치하는 해시태그 검색. 예: `맛집`, `성수동`.                                         |
      | `type`      | `string` | 모든 타입 조회      | 게시물의 type별로 조회.                                                                      |
      | `order_by`  | `string` | `created_at`        | 정렬 기준: `created_at`, `updated_at`, `like_count`, `share_count`, `view_count`.             |
      | `search_by` | `string` | `title,content`     | 검색 기준: 제목, 내용 또는 제목 + 내용 검색.                                                 |
      | `search`    | `string` |                     | 검색 키워드. 포함된 게시물 검색.                                                             |
      | `page_count`| `number` | 10                  | 페이지당 게시물 수.                                                                          |
      | `page`      | `number` | 0                   | 조회하려는 페이지.                                                                           |
      
      </details>
    - 게시물 목록 API에서 `content`는 최대 **20자**까지만 표시.
    - 불필요한 필드는 제외.
  
  - 게시물 상세
    - 특정 게시물의 **모든 필드 값**을 확인.
    - API 호출 시 해당 게시물의 조회수 증가.
    
  - 게시물 좋아요
    - 좋아요 클릭 시 각 SNS 별 명시된 외부 API 를 호출하여 내부 서비스에서 좋아요 수 증가
   
  - 게시물 공유
    - 게시물은 외부 서비스에서 관리되며, 각 SNS 별로 아래 API 호출

  - 게시물 통계
    <details>
      <summary>다음 파라미터 사용</summary>

      
    | Parameter   | Type         | Default             | Description                                                                                  |
    |-------------|--------------|---------------------|----------------------------------------------------------------------------------------------|
    | `hashtag`   | `string`     | 본인 계정          | 조회할 해시태그.                                                                             |
    | `type`      | `enum`       | 필수 값            | `date` 또는 `hour`.                                                                         |
    | `start`     | `date`       | 오늘로부터 7일 전  | 조회 시작일 (예: `2023-10-01`).                                                             |
    | `end`       | `date`       | 오늘               | 조회 종료일 (예: `2023-10-25`).                                                             |
    | `value`     | `string`     | `count`            | `count`, `view_count`, `like_count`, `share_count` 중 하나.                                 |
  
  
    </details>
    
    - `value`
        - `count` 일 시, 게시물 개수
        - `view_count` 조회된 게시물 들의 `view_count` 의 `sum` (`like_count`,`share_count` 도 동일)
    - `?value=count&type=date` 일시, `start` ~ `end` 기간내 (시작일, 종료일 포함) 해당 `hashtag` 가 포함된 게시물 수를 일자별로 제공
        - 최대 한달(30일) 조회 가능
    - `?value=count&type=hour` 일시, `start` ~ `end` 기간내 (시작일, 종료일 포함) 해당 `hashtag` 가 포함된 게시물 수를 시간별로 제공
        - `start` 일자의 00시 부터 1시간 간격으로.
        - 초대 일주일(7일) 조회 가능

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

<details>
<summary>ERD</summary>
  
  ![image](https://github.com/user-attachments/assets/fb2d6a6b-31f7-4439-bb14-6934af4e3285)

</details>

<details>
<summary>디렉터리 구조</summary>
  
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

