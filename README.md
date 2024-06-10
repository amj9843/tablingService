# 매장 테이블 예약 서비스 API

## Development Environment
- Intellij IDEA Community
- Java 17
- Gradle 8.7
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
  
  - POST ```/sign/up```
  - 중복 ID는 허용하지 않음
  - 패스워드는 암호화된 형태로 저장됨
  - 이름은 20자 이내, 핸드폰번호는 "010-0000-0000" 형식 혹은 "00000000000" 형식, type는 일반 사용자인 "USER" 혹은 매장 관리자 "PARTNER"
  - 입력 파라미터
    * ##### body
    ```json
    {
      "id": "string", 
      "password": "string", 
      "name": "string", 
      "phoneNumber": "string",
      "type": "string"
    }
    ```
  - 출력 결과
    ```json
    {
      "data": {
        "userId": 0,
        "id": "string"
      },
      "code": "string",
      "message": "string"
    }
    ```
  </details>
  <details>
  <summary>로그인 API</summary>

    - POST ```/sign/in```
    - 회원가입이 되어있고, 아이디/패스워드가 일치하는 경우 JWT 발급
    - 입력 파라미터
      * ##### body
      ```json
      {
        "id": "string",
        "password": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "token": "string"
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>로그아웃 API</summary>

    - GET ```/sign/out```
    - 로그인이 되어있는 경우 로그아웃
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>회원정보 수정 API</summary>

    - PATCH ```/modify```
    - 현재의 비밀번호(originPassword)를 제외하고선 변경이 없는 항목은 제거
    - 현재 비밀번호 입력값이 등록된 정보와 다를 땐 실행되지 않음
    - 이름은 20자 이내, 핸드폰번호는 "010-0000-0000" 형식 혹은 "00000000000" 형식
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "originPassword": "string",
        "password": "string",
        "username": "string",
        "phoneNumber": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>회원탈퇴 API</summary>

    - DELETE ```/expire```
    - 현재 로그인 중인 사용자가 자신의 비밀번호를 알맞게 입력하였을 경우 회원 정보 삭제
    - 파트너의 경우 관리중인 매장에 진행중인 예약이 있는 경우 탈퇴 불가
    - 일반 사용자의 경우 진행중인 예약이 있는 경우 탈퇴 불가
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "password": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
- ### /store : 매장 관리
  <details>
  <summary>전체 매장 조회 API</summary>

    - GET ```/search```
    - 정렬 기준 미선택 시 이름순 반환
    - 입력 파라미터
        * ##### param
      | key  |                                                                     value                                                                     |
      |:----:|:---------------------------------------------------------------------------------------------------------------------------------------------:|
      | page |                                                              가져올 페이지 번호(0부터 시작)                                                               |
      | size |                                                                 페이지당 보일 목록 개수                                                                 |
      | criteria | 목록 정렬 순<br><br>- 이름순(NAMEASC)<br>- 평점 높은순(RATEHIGHEST)<br>- 평점 낮은순(RATELOWEST)<br>- 리뷰 많은순(REVIEWCOUNTHIGHEST)<br>- 리뷰 적은순(REVIEWCOUNTLOWEST) |
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": {
          "content": [
            {
                "storeId": 0,
                "name": "string",
                "location": "string",
                "description": "",
                "rate": 0.0,
                "reviewCount": 0
            }
          ],
          "pageable": {
            "pageNumber": 0,
            "pageSize": 0,
            "sort": {
              "empty": false,
              "unsorted": false,
              "sorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
          },
          "last": true,
          "totalPages": 0,
          "totalElements": 0,
          "size": 0,
          "number": 0,
          "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        }
      }
      ```
  </details>
  <details>
  <summary>매장 검색 API</summary>

    - GET ```/search/{word}```
    - 검색하고자 하는 단어를 입력값(word)으로 받으면 이름, 위치, 설명에 해당 입력값이 포함되는 매장 목록 호출
    - 정렬 기준 미선택 시 이름순 반환
    - 입력 파라미터
      * ##### param
      | key  |                                                                     value                                                                     |
      |:----:|:---------------------------------------------------------------------------------------------------------------------------------------------:|
      | page |                                                              가져올 페이지 번호(0부터 시작)                                                               |
      | size |                                                                 페이지당 보일 목록 개수                                                                 |
      | criteria | 목록 정렬 순<br><br>- 이름순(NAMEASC)<br>- 평점 높은순(RATEHIGHEST)<br>- 평점 낮은순(RATELOWEST)<br>- 리뷰 많은순(REVIEWCOUNTHIGHEST)<br>- 리뷰 적은순(REVIEWCOUNTLOWEST) |
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": {
          "content": [
            {
                "storeId": 0,
                "name": "string",
                "location": "string",
                "description": "",
                "rate": 0.0,
                "reviewCount": 0
            }
          ],
          "pageable": {
            "pageNumber": 0,
            "pageSize": 0,
            "sort": {
              "empty": false,
              "unsorted": false,
              "sorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
          },
          "last": true,
          "totalPages": 0,
          "totalElements": 0,
          "size": 0,
          "number": 0,
          "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        }
      }
      ```
  </details>
  <details>
  <summary>(파트너용) 매장 목록 조회 API</summary>

    - GET ```/my-store```
    - 본인이 등록한 모든 매장 목록을 반환
    - 본인의 userId 외의 값을 입력한 경우 권한 오류 반환
    - 반환 결과는 Page 인터페이스 형태로 매장 리스트를 입력한 정렬 기준으로 정렬해 반환(미선택 시 기본 최근 등록순)
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### param
      | key  |                                                                     value                                                                     |
      |:----:|:---------------------------------------------------------------------------------------------------------------------------------------------:|
      | page |                                                              가져올 페이지 번호(0부터 시작)                                                               |
      | size |                                                                 페이지당 보일 목록 개수                                                                 |
      | criteria | 목록 정렬 순<br><br>- 이름순(NAMEASC)<br>- 평점 높은순(RATEHIGHEST)<br>- 평점 낮은순(RATELOWEST)<br>- 리뷰 많은순(REVIEWCOUNTHIGHEST)<br>- 리뷰 적은순(REVIEWCOUNTLOWEST) |
    - 출력 결과
      ```json
      {
        "data": {
          "content": [
            {
                "storeId": 0,
                "name": "string",
                "location": "string",
                "description": "",
                "rate": 0.0,
                "reviewCount": 0
            }
          ],
          "pageable": {
            "pageNumber": 0,
            "pageSize": 0,
            "sort": {
              "empty": false,
              "unsorted": false,
              "sorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
          },
          "last": true,
          "totalPages": 0,
          "totalElements": 0,
          "size": 0,
          "number": 0,
          "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장별 예약 가능한 상세정보 조회 API</summary>

    - GET ```/detail/{storeId}```
    - 권한(로그인) 필요 없음
    - 매장을 등록할 때 발급받았던 storeId를 입력하였을 경우 매장의 상세 정보 반환
    - 입력 파라미터
      * ##### param
      | key  |       value        |
      |:----:|:------------------:|
      | page | 가져올 페이지 번호(0부터 시작) |
      | size |   페이지당 보일 목록 개수    |
    - 출력 결과
      ```json
      {
        "data": {
        "storeId": 0,
        "name": "string",
        "location": "string",
        "description": "string",
        "rate": 0.0,
        "reviewCount": 0,
        "details": {
            "content": [
                {
                    "storeDetailId": 0,
                    "reservationTime": "string",
                    "totalHeadCount": 0,
                    "nowHeadCount": 0
                }
            ],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10,
                "sort": {
                    "empty": true,
                    "sorted": false,
                    "unsorted": true
                },
                "offset": 0,
                "unpaged": false,
                "paged": true
            },
            "last": true,
            "totalPages": 1,
            "totalElements": 2,
            "size": 10,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 2,
            "empty": false
            }
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 정보 등록 API</summary>

    - POST ```/regist/store```
    - 파트너로 로그인하였고 매장 등록에 필요한 모든 정보가 입력되었을 경우 매장 등록
    - 파트너 회원이 이미 같은 이름, 같은 주소로 등록한 매장이 존재하는 경우 400 status 코드와 에러메세지 반환
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "name": "string",
        "location": "string",
        "description": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "ownerId": 0,
          "storeId": 0,
          "name": "string",
          "location": "location",
          "description": "description"
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 상세 정보 등록 API</summary>

    - POST ```regist/details/{storeId}```
    - 파트너로 로그인하였고 매장 등록에 필요한 모든 정보가 입력되었을 경우 매장 등록
    - 파트너 회원이 이미 같은 이름, 같은 주소로 등록한 매장이 존재하는 경우 400 status 코드와 에러메세지 반환
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "storeDetails": [
          {
            "reservationTime": "string",
            "headCount": 0
          }
        ]
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "storeId": 0,
          "registedStoreDetails": [
            {
              "storeDetailId": 0,
              "reservationTime": "string",
              "headCount": 0
            }
          ]
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 정보 수정 API</summary>

    - PATCH ```/update/{storeId}```
    - 변경이 없는 항목은 제거(이름, 위치정보엔 빈 값 입력 불가)
    - 로그인한 사용자가 권한이 있는 매장만 수정 가능
    - 입력 파라미터
       * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
       * ##### body
      ```json
      {
        "name": "string",
        "location": "string",
        "description": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 상세정보 수정 API</summary>

    - PATCH ```/update/detail/{storeDetailId}```
    - 인원수 필수 입력
    - 현재 예약 신청, 혹은 예약 승인된 총 인원수보다 변경하려는 예약 가능 총 수가 더 적으면 에러 반환
    - 입력 파라미터
       * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
       * ##### body
      ```json
      {
        "headCount": 0
      }
      ```
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 정보 삭제 API</summary>

    - DELETE ```/delete/{storeId}```
    - 매장을 등록했던 파트너가 등록 시 발급받았던 storeId를 입력했을 경우 해당하는 매장 정보 삭제
    - 매장에 허가를 기다리는 예약(신청 상태), 진행중인 예약(승인 상태)이 없는 경우 동작
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>매장 상세정보 삭제 API</summary>

    - DELETE ```/delete/detail/{storeDetailId}```
    - 매장을 등록했던 파트너가 등록 시 발급받았던 storeDetailId를 입력했을 경우 해당하는 매장 상세정보 삭제
    - 허가를 기다리는 예약(신청 상태), 진행중인 예약(승인 상태)이 없는 경우 동작
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
- ### /reservation : 예약 관리
  <details>
  <summary>예약 신청 API</summary>

    - POST ```/apply/{storeDetailId}```
    - 사용자가 예약 가능한 매장의 상세정보 식별번호 입력, 예약할 인원수 입력
    - 이미 해당 시간에 예약한 정보가 있다면 에러 반환
    - 예약 가능한 인원수보다 예약할 인원수가 많을 시 에러 반환
    - 사용자(USER) 권한 필수
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "headCount": 0
      }
      ```
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": {
          "reservationId": 0,
          "time": "string",
          "headCount": 0,
          "status": "string"
        }
      }
      ```
  </details>
  <details>
  <summary>예약 정보 수정 API</summary>

    - PATCH ```/update/{reservationId}```
    - 사용자가 예약 내역 식별번호에 해당하는 본인의 예약 내용 변경
    - 예약 상태가 '신청'일 경우만 수정 가능
    - 매장 상세번호 변경 시 이미 예약한 상세번호로 변경 불가
    - 인원수 변경 시 예약 인원수가 수용 가능 인원을 넘어설 경우 에러 반환
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body (변경이 없는 항목은 제거)
      ```json
      {
        "storeDetailId": 0,
        "headCount": 0
      }
      ```
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>예약 취소 API</summary>

    - PATCH ```/cancel/{reservationId}```
    - 사용자가 본인이 예약한 내용 취소
    - 단, 예약 상태가 방문완료인 경우는 취소 불가
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>예약 삭제 API</summary>

    - DELETE ```/delete/{reservationId}```
    - 사용자가 본인이 예약한 내용 삭제
    - 단, 예약 상태가 취소, 혹은 거절인 경우만 삭제 가능
    - 입력 파라미터
        * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>사용자 날짜별 예약 내역 조회 API</summary>

    - GET ```/list```
    - 사용자의 예약(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간) 리스트 반환
    - 예약 시간순 정렬 고정
    - 입력 파라미터
      * ##### param
      |  key   |                                                                    value                                                                     |
      |:------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|
      | status | 확인할 예약 상태(미입력시 전체)<br><br>- 예약 신청 상태(APPLIED)<br>- 예약 승인 상태(APPROVED)<br>- 예약 거절 상태(DENIED)<br>- 예약 취소 상태(CANCELED)<br>- 방문 완료 상태(COMPLETED) |
      |  date  |                                                           확인할 날짜('yyyy-MM-dd' 형식)                                                            |
      |  page  |                                                                   확인할 페이지                                                                    |
      |  size  |                                                                  페이지당 목록 개수                                                                  |
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": {
          "content": [
              {
                  "reservationTime": "string",
                  "reservationInfo": [
                      {
                          "reservationId": 0,
                          "store": {
                              "storeId": 0,
                              "name": "string",
                              "location": "string",
                              "description": "string"
                          },
                          "headCount": 0,
                          "status": "string"
                      }
                  ]
              }
          ],
          "pageable": {
              "pageNumber": 0,
              "pageSize": 10,
              "sort": {
                    "empty": true,
                    "unsorted": true,
                    "sorted": false
              },
              "offset": 0,
              "paged": true,
              "unpaged": false
          },
          "totalElements": 0,
          "totalPages": 0,
          "last": true,
          "size": 0,
          "number": 0,
          "sort": {
              "empty": true,
              "unsorted": true,
              "sorted": false
            },
          "first": true,
          "numberOfElements": 0,
          "empty": false
          },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>사용자의 예약 내역 확인 API</summary>

    - GET ```/detail/{reservationId}```
    - 사용자가 입력한 예약 식별번호로 예약의 상세내역확인
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "createAt": "string",
          "store": {
            "storeId": 0,
            "name": "string",
            "location": "string",
            "description": "string"
          },
          "reservation": {
            "reservationId": 0,
            "time": "string",
            "headCount": 0,
            "status": "string"
          }
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장 예약 내역 확인 API</summary>

    - GET ```/store/{storeId}```
    - 매장을 등록한 파트너 이용자가 확인하려는 매장의 예약(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간) 리스트 조회
    - 예약 시간순 정렬 고정
    - 입력 파라미터
      * ##### param
      |  key   |                                                                    value                                                                     |
      |:------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|
      | status | 확인할 예약 상태(미입력시 전체)<br><br>- 예약 신청 상태(APPLIED)<br>- 예약 승인 상태(APPROVED)<br>- 예약 거절 상태(DENIED)<br>- 예약 취소 상태(CANCELED)<br>- 방문 완료 상태(COMPLETED) |
      |  date  |                                                           확인할 날짜('yyyy-MM-dd' 형식)                                                            |
      |  page  |                                                                   확인할 페이지                                                                    |
      |  size  |                                                                  페이지당 목록 개수                                                                  |
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": {
          "content": [
              {
                  "reservationTime": "string",
                  "reservationInfo": [
                      {
                          "reservationId": 0,
                          "user": {
                              "id": 0,
                              "name": "string"
                          },
                          "headCount": 0,
                          "status": "APPLIED"
                      }
                  ]
              }
          ],
          "pageable": {
              "pageNumber": 0,
              "pageSize": 0,
              "sort": {
                  "empty": true,
                  "unsorted": true,
                  "sorted": false
              },
              "offset": 0,
              "unpaged": false,
              "paged": true
          },
          "last": true,
          "totalElements": 0,
          "totalPages": 0,
          "size": 0,
          "number": 0,
          "sort": {
              "empty": true,
              "unsorted": true,
              "sorted": false
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>매장별 전체 예약 신청 내역 확인 API</summary>

    - GET ```/store/all-apply/{storeId}```
    - 매장을 등록한 파트너 이용자가 확인하려는 매장의 전체 예약 신청 리스트 조회
    - 예약 시간순 정렬 고정
    - 입력 파라미터
        * ##### param
      |  key   |   value    |
      |:------:|:----------:|
      |  page  |  확인할 페이지   |
      |  size  | 페이지당 목록 개수 |
        * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": {
          "content": [
              {
                  "reservationTime": "string",
                  "reservationInfo": [
                      {
                          "reservationId": 0,
                          "user": {
                              "id": 0,
                              "name": "string"
                          },
                          "headCount": 0,
                          "status": "APPLIED"
                      }
                  ]
              }
          ],
          "pageable": {
              "pageNumber": 0,
              "pageSize": 0,
              "sort": {
                  "empty": true,
                  "unsorted": true,
                  "sorted": false
              },
              "offset": 0,
              "unpaged": false,
              "paged": true
          },
          "last": true,
          "totalElements": 0,
          "totalPages": 0,
          "size": 0,
          "number": 0,
          "sort": {
              "empty": true,
              "unsorted": true,
              "sorted": false
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>관리자의 예약 승인 API</summary>

    - PATCH ```approve/{reservationId}```
    - 매장의 파트너 이용자가 일반 이용자로부터 신청된 예약 허가
    - 신청 상태의 예약 내역만 승인 가능
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>관리자의 예약 거절 API</summary>

    - PATCH ```/deny/{reservationId}```
    - 매장의 파트너 이용자가 일반 이용자로부터 신청된 예약 거절
    - 신청 상태의 예약 내역만 거절 가능
    - 입력 파라미터
        * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "data": null,
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>방문 완료 API</summary>

    - PATCH ```/kiosk/reservation-id```
    - 권한(로그인) 미필요
    - 승인된 예약 시간 30분 전 ~ 10분 전 도착만 방문 완료 가능
    - 입력 파라미터
      * ##### body
      ```json
      {
        "reservationId": 0
      }
      ```
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
- ### /review : 리뷰 관리
  <details>
  <summary>리뷰 작성 API</summary>

    - POST ```/regist/{reservationId}```
    - 방문 완료한 사용자가 예약과 관련된 리뷰 작성 가능
    - 한 예약내역 당 하나의 리뷰만 작성 가능
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body
      ```json
      {
        "rate": 0,
        "context": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "data": {
          "reviewedId": 0,
          "rate": 0,
          "context": "string"
        },
        "code": "string",
        "message": "string"
      }
      ```
  </details>
  <details>
  <summary>리뷰 수정 API</summary>

    - PATCH ```/update/{reviewId}```
    - 리뷰 작성자가 작성했던 리뷰 수정
    - 입력 파라미터
      * ##### header
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
      * ##### body (변경이 없는 항목은 제거)
      ```json
      {
        "rate": 0,
        "context": "string"
      }
      ```
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>리뷰 삭제 API</summary>

    - DELETE ```delete/{reviewedId}```
    - 리뷰 작성자, 혹은 매장 파트너가 삭제할 reviewedId 입력 시 리뷰 삭제
    - 입력 파라미터
      |key|value|
      |:---:|:---:|
      |Authorization|Bearer 로그인 시 발급받은 토큰|
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": null
      }
      ```
  </details>
  <details>
  <summary>매장별 리뷰 조회 API</summary>

    - GET ```/store/{storeId}```
    - 정렬 기준 미선택 시 최근 등록순 반환
      * ##### param
      | key  |                                                                     value                                                                     |
      |:----:|:---------------------------------------------------------------------------------------------------------------------------------------------:|
      | page |                                                              가져올 페이지 번호(0부터 시작)                                                               |
      | size |                                                                 페이지당 보일 목록 개수                                                                 |
      | criteria | 목록 정렬 순<br><br>- 최근 등록순(CREATEDATDESC)<br>- 등록순(CREATEDATASC)<br>- 평점 높은순(RATEHIGHEST)<br>- 평점 낮은순(RATELOWEST) |
  - 출력 결과
    ```json
    {
      "code": "string",
      "message": "string",
      "data": {
        "content": [
            {
                "createdAt": "string",
                "updatedAt": "string",
                "reviewId": 0,
                "rate": 0,
                "context": "string"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 0,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "last": true,
        "totalPages": 0,
        "totalElements": 0,
        "size": 0,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 0,
        "empty": false
      }
    }
    ```
  </details>
  <details>
  <summary>유저별 리뷰 조회 API</summary>

    - GET ```/user/{userId}```
    - 정렬 기준 미선택 시 최근 등록순 반환
       * ##### param
      | key  |                                                                     value                                                                     |
      |:----:|:---------------------------------------------------------------------------------------------------------------------------------------------:|
      | page |                                                              가져올 페이지 번호(0부터 시작)                                                               |
      | size |                                                                 페이지당 보일 목록 개수                                                                 |
      | criteria | 목록 정렬 순<br><br>- 최근 등록순(CREATEDATDESC)<br>- 등록순(CREATEDATASC)<br>- 평점 높은순(RATEHIGHEST)<br>- 평점 낮은순(RATELOWEST) |
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": {
          "content": [
              {
                  "createdAt": "string",
                  "updatedAt": "string",
                  "reviewId": 0,
                  "rate": 0,
                  "context": "string"
              }
          ],
          "pageable": {
              "pageNumber": 0,
              "pageSize": 0,
              "sort": {
                  "empty": false,
                  "sorted": true,
                  "unsorted": false
              },
              "offset": 0,
              "unpaged": false,
              "paged": true
          },
          "last": true,
          "totalPages": 0,
          "totalElements": 0,
          "size": 0,
          "number": 0,
          "sort": {
              "empty": false,
              "sorted": true,
              "unsorted": false
          },
          "first": true,
          "numberOfElements": 0,
          "empty": false
        }
      }
      ```
  </details>
  <details>
  <summary>상세 리뷰 조회 API</summary>

    - PATCH ```/{reviewId}/detail```
    - 리뷰 상세내역 확인
    - 출력 결과
      ```json
      {
        "code": "string",
        "message": "string",
        "data": {
          "createAt": "string",
          "updateAt": "string",
          "reservation": {
            "id": 0,
            "time": "string",
            "headCount": 0
          },
          "user": {
            "id": 0,
            "name": "string"
          },
          "rate": 0,
          "context": "string"
        }
      }
      ```
  </details>
