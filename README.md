# 매장 테이블 예약 서비스 API

## Development Environment
- Intellij IDEA Community
- Java 17
- Gradle
- Spring Boot 3.2.5

## Tech Stack
- Spring Boot, Spring Security, Spring Data Jpa
- MySQL
- Redis
- JWT
- lombok

## API 명세서
- ### /auth : 회원 관리
  <details>
  <summary>회원가입 API</summary>
  
  - POST ```/signup```
  - 중복 ID는 허용하지 않음
  - 패스워드는 암호화된 형태로 저장됨
  - 입력 파라미터
    ```json
    {
      "id": String, 
      "password": String, 
      "name": String, 
      "phoneNumber": String, //입력했는데 000-0000-0000 형식이 아닐 경우 오류
      "type": String //PARTNER(파트너) | USER(일반 사용자) 중 하나 선택
    }
    ```
  - 출력 결과
    ```json
    {
      "data": {
        "userId": Long,
        "id": String
      },
      "code": String,
      "message": String
    }
    ```
  </details>
  <details>
  <summary>로그인 API</summary>

    - POST ```/signin```
    - 로그인 API
    - 회원가입이 되어있고, 아이디/패스워드가 일치하는 경우 JWT 발급
    - 입력 파라미터
      ```json
      {
        "id": String,
        "password": String
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "token": String
        },
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>회원탈퇴 API</summary>

    - DELETE ```/{userId}```
    - 회원탈퇴 API
    - 현재 로그인 중인 사용자가 자신의 userId를 알맞게 입력하였을 경우 회원 정보 삭제
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |userId|회원가입 시 받았던 userId|
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
- ### /store : 매장 관리
  <details>
  <summary>매장 검색 API</summary>

    - GET ```/search```
    - 검색하고자 하는 단어를 입력값으로 받고, 해당 단어가 포함되어있는 매장 리스트를 입력한 정렬 기준으로 정렬해 반환(미선택 시 기본 이름순)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |search|검색할 단어|
      |orderBy|정렬 기준(NAME: 이름순, RATESCORE: 평점순, RATECOUNT: 리뷰순)|
    - 출력 결과
      ```json
      {
        "data": [
          {
            "storeId": Long,
            "storeName": String,
            "location": String,
            "description": String,
            "rate": {
              "count": Long,
              "score": Double
            }
          }, ...
        ]
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>매장 상세정보 API</summary>

    - GET ```/details/{storeId}```
    - 매장을 등록할 때 발급받았던 storeId를 입력하였을 경우 매장의 상세 정보 반환
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |storeId|상세정보를 확인할 매장의 storeId|
    - 출력 결과
      ```json
      {
        "data": {
          "storeId": Long,
          "storeName": String,
          "location": String,
          "description": String,
          "rate": {
            "count": Long,
            "score": Double
          },
          "maxPeopleForTime": Integer,
          "reservations": {DateTime: Integer, DateTime: Integer, ...}, //매장에서 예약받는 시간대에 예약한 사람 수
          "reviews": [{
            "usedDate": String,
            "createdAt": String,
            "modifiedAt": String,
            "rate": Integer,
            "context": String
          }, ...]
        },
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>매장 목록 확인 API</summary>

    - GET
    - 서비스에서 관리하고 있는 모든 매장 목록을 반환
    - 반환 결과는 Page 인터페이스 형태로 매장 리스트를 입력한 정렬 기준으로 정렬해 반환(미선택 시 기본 이름순)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |orderBy|정렬 기준(NAME: 이름순, RATESCORE: 평점순, RATECOUNT: 리뷰순)|
    - 출력 결과
      ```json
      {
        "data": [
          {
            "storeId": Long,
            "storeName": String,
            "location": String,
            "description": String,
            "rate": {
              "count": Long,
              "score": Double
            }
          }, ...
        ]
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>매장 정보 등록 API</summary>

    - POST ```/regist```
    - 파트너로 로그인하였고 매장 등록에 필요한 모든 정보가 입력되었을 경우 매장 등록
    - 파트너 회원이 이미 같은 이름, 같은 주소로 등록한 매장이 존재하는 경우 400 status 코드와 에러메세지 반환
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      ```json
      {
        "storeName": String,
        "location": String,
        "description": String,
        "reservationTimes": [String], //"yyyy-mm-dd hh:mm" 형태의 시간 리스트로 작성
        "maxPeopleForTime": Integer //0 이상으로 작성
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "storeId": Long
        },
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>매장 정보 삭제 API</summary>

    - DELETE ```/{storeId}```
    - 매장을 등록했던 파트너가 등록 시 발급받았던 storeId를 입력했을 경우 해당하는 매장 정보 삭제
    - 진행중인 예약이 없는 경우 동작
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |storeId|매장 등록 시 받았던 storeId|
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
- ### /reservation : 예약 관리
  <details>
  <summary>매장 예약 API</summary>

    - POST ```/{storeId}```
    - 사용자가 매장의 예약 가능 때에 예약
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |storeId|예약할 매장의 storeId|
      ```json
      {
        "reservationDateTime": String,
        "storeId": Long
        "numberOfPeople": Integer
      }
      ```
    - 출력 결과
      ```json
      {
        "reservedId": Long,
        "code": String,
        "message": String
      }, ...]
      ```
  </details>
  <details>
  <summary>예약 정보 수정 API</summary>

    - PATCH ```/{reservedId}```
    - 사용자가 본인의 예약 내용 변경(조건은 예약 등록 때와 동일, 변경 없음 불가, 예약 장소 변경 불가)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|변경할 예약의 reservedId|
      ```json
      //변경할 내용만 작성
      {
        "reservationDateTime": String,
        "numberOfPeople": Integer
      }
      ```
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }, ...]
      ```
  </details>
  <details>
  <summary>예약 취소 API</summary>

    - DELETE ```/{reservedId}```
    - 사용자가 예약한 내용 취소
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|취소할 예약의 reservedId|
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }, ...]
      ```
  </details>
  <details>
  <summary>사용자 예약 내역 확인 API</summary>

    - GET ```/list```
    - 사용자가 자신이 예약한 내역 확인(예약 시간, 매장명순 정렬)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": [{
          "reservedId": Long,
          "createAt": String,
          "reservationDateTime": String,
          "storeName": String,
          "location": String,
          "description": String
          "numberOfPeople": Integer,
          "status": String
        }
        "code": String,
        "message": String
      }, ...]
      ```
  </details>
  <details>
  <summary>매장 예약 내역 확인 API</summary>

    - GET ```/list/{storeId}```
    - 매장을 등록한 파트너 이용자가 확인하려는 매장의 기간 내 예약 목록 확인(정렬: 날짜, 시간, 이름순)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |storeId|확인하려는 매장의 storeId|
      ```json
      {
        "startDate": String,
        "endDate": String
      }
      ```
    - 출력 결과
      ```json
      {
        "data": [{
          "reservedId": Long,
          "createAt": String,
          "reservationDateTime": String,
          "userId": String,
          "userName": String,
          "userPhoneNumber": String,
          "numberOfPeople": Integer,
          "status": String
        }
        "code": String,
        "message": String
      }, ...]
      ```
  </details>
  <details>
  <summary>예약 승인/거절 API</summary>

    - PATCH ```/{reservedId}```
    - 매장의 파트너 이용자가 등록된 예약을 받을지 말지 결정
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|승인/거절하려는 reservedId|
      ```json
      {
        "status": Boolean
      }
      ```
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>방문 완료 API</summary>

    - PATCH ```/confirm/{reservedId}```
    - 승인된 예약 시간 전후 10분 이내로 방문 확인 가능
    - 입력 파라미터(id와 phoneNumber의 경우 둘 중 하나만 선택)
      |key|value|
      |:---:|:---:|
      |reservedId|예약 시 받았던 reservedId|
      |id|예약자의 아이디|
      |phoneNumber|예약자의 핸드폰번호|
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
- ### /review : 리뷰 관리
  <details>
  <summary>리뷰 작성 API</summary>

    - POST ```/{reservedId}```
    - 예약 승인 이후엔 사용자가 예약과 관련된 리뷰 작성 가능
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|방문 완료한, 리뷰를 작성하려는 매장의 관련 reservedId|
      ```json
      {
        "rate": Integer,
        "context": String
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "reviewedId": Long
        },
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>리뷰 수정 API</summary>

    - PATCH ```/{reviewedId}```
    - 리뷰 작성자 리뷰 수정
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|리뷰 작성 시 발급받은 reservedId|
      ```json
      //필요한 변경값만 작성
      {
        "rate": Integer,
        "context": String
      }
      ```
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>리뷰 삭제 API</summary>

    - DELETE ```/{reviewedId}```
    - 리뷰 작성자, 혹은 매장 파트너가 삭제할 reviewedId 입력 시 리뷰 삭제
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      |reservedId|리뷰 작성 시 발급받은 reservedId|
    - 출력 결과
      ```json
      {
        "code": String,
        "message": String
      }
      ```
  </details>
  <details>
  <summary>매장별 리뷰 조회 API</summary>

    - GET ```/{storeId}```
    - storeId에 해당하는 매장의 리뷰 목록 조회
    - 반환 결과는 Page 인터페이스 형태로 입력한 정렬 기준으로 정렬해 반환(미선택 시 기본 최근 등록순)
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |orderBy|정렬 기준(LATEST: 최근 등록순, EARLIEST: 등록순, RATEGREATER: 평점 높은 순, RATELESS: 평점 낮은 순)|
    - 출력 결과
      ```json
      {
        "data": [{
          "usedDate": String,
          "createdAt": String,
          "modifiedAt": String,
          "rate": Integer,
          "context": String
        }, ...]
        "code": String,
        "message": String
      }
      ```
  </details>
