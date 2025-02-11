## 📌 캐시(Cache)

### 👉 자주 접했던 캐시 그림

![image](https://github.com/user-attachments/assets/1a4bd559-90cc-4ba9-b3cf-903fa36a09ee)

우리는 이 그림을 보고서 아래로 가면 ‘싸다(Cheaper) & 느리다(Slower) & 크다(Bigger)’ 위로 갈 수록 ‘비싸다(more Expensive) & 빠르다(Faster) & 작다(Smaller)’ 라고 외우고는 했습니다.  
캐시는 다른 메모리에 비해서 **<ins>비싸고</ins>**, **<ins>빠르고</ins>**, **<ins>작다</ins>** 라고 생각 할 수 있습니다.

또 컴퓨터를 구매 하면서도 스펙 상세 정보에도 자주 마주칠 수 있었습니다.

<img width="955" alt="스크린샷 2025-02-02 오전 1 08 27" src="https://github.com/user-attachments/assets/e703b4a0-c2f1-4225-a0a6-48c45d04d53b" />

### 👉 캐시는 왜 빠를까?

1. 물리적으로 가깝다.

하드웨어로 살펴보면 캐시는 CPU 옆에 있기 때문에 외부 저장장치에 비하면 더 빠르게 데이터를 가져올 수 있습니다.  
만약 캐시에 데이터가 없다면 데이터를 요청하고 응답 받는데 더 많은 장치와 절차가 생기게 되어 느립니다.

![image](https://github.com/user-attachments/assets/78d910f5-ee4c-4e65-8007-60f903394d5d)

#### ✅ Cache Hit

참조한 메모리 혹은 데이터가 캐시에 이미 로드되어 있는 경우 Cache Hit 라고 하며 빠르게 요청에 대한 응답을 반환할 수 있습니다.

```
ex1) CPU -> Cache -> END

ex2) Request -> Web Server -> Redis -> Web Server -> Response
```

#### ✅ Cache Miss

Cache Hit 와 반대로 Cache 에 필요한 데이터가 없을 때를 말합니다.

```
ex1) CPU -> Cache(LV1 ~ LV4) -> RAM -> SSD -> HDD
            (Miss)

※ RAM 에서 프로세스가 참조한 메모리를 못 찾게 된다면 Page Fault 발생으로 인터럽트와 함께 페이지 교체 혹은 로드까지의 과정 (Disk I/O 발생)

ex2) Request -> Web Server -> Redis -> -> Web Server -> DB -> Web Server -> Redis & Response
                              (Miss)                                       (Caching)
```

2. 반복적인 작업을 감소 시킨다.

소프트웨어적으로 캐시를 바라본다면 자주 사용되는 데이터를 저장하기 때문에 불필요한 연산들이 줄어서 빠른 속도의 이점을 얻는다. (메모리 효율 📉 / 속도 📈)

### 👉 캐시는 어디에 사용될까?

많은 곳에 쓰인다....  

사실 캐시의 개념을 살펴보기 위해서 하드웨어 혹은 OS 레벨로 내려갈 필요 없이 우리가 사용하나 Java compiler 도 사용하고 volatile 키워드를 통해서 Main Memory 에 저장하고 읽는 것 또한 캐시의 개념이다.

<img width="500" alt="스크린샷 2025-02-02 오전 1 47 17" src="https://github.com/user-attachments/assets/385280bf-a8dc-4b81-a4de-d5c980b05550" />

우리가 사용하는 DB 또한 엔진에 캐시를 위한 공간이 마련되어 있다.

[ Oracle ]

![image1](https://github.com/user-attachments/assets/2cd7cd93-28d6-46d8-aa0c-4bd2dcc78ba3)

[ MySql ]

![image2](https://github.com/user-attachments/assets/53bfc662-94db-4ec3-b6a7-3225e4b3c4d6)

DB 가 쿼리를 캐싱하는 것 처럼 웹 서버에서 Http Method 가 Get 이라고 가정 했을 때 매번 같은 요청이 온다면 DB 에서 조회하는 게 아닌 캐싱된 결과 데이터를 그대로 반환 해준다면? (싱크가 맞다고 가정 했을 때)

그렇다 캐시의 개념은 항상 똑같으며, 사용하는 곳과 도구가 다른 것 뿐이다.

### 👉 캐시는 무엇일까?

캐시는 성능을 위한 최적화 도구다.  
따라서 Redis 는 캐시가 아닌 성능 최적화를 돕는 아닌 도구중 하나이다.

### 👉 우리가 캐시를 공부하고 사용하는 목적은?

많은 트래픽이 몰려도 DB 부하를 최소화 하여 어플리케이션 서비스를 장애 없이 유지하기 위해서  
&   
다중 인스턴스 환경에서 각 어플리케이션 인스턴스가 바라보는 데이터의 정합성을 맞추기 위해서!! (그래서 동시성 제어도 같이 배웠으니까)

---

## 📌 캐시(Cache) 전략

### 👉 왜 전략이 필요한가?

시스템 속도 개선을 위해서 RAM 의 메모리를 많이 사용하면 OOM 에 대해서 생각하지 않을 수 없다.

또 데이터 정합성이 깨질 수 있는데 이는 캐시(Cache Store) 와 데이터 베이스(Data Store) 두 곳에 데이터를 저장하기 때문에 적절한 전략이 없다면 데이터가 서로 다를 수 있다.

### 👉 캐시 읽기 전략(Read Cache Strategy)

#### 1️⃣Look Aside (Cache Aside)

Cache 를 우선해서 조회한 후 Cache Miss 가 발생하면 DB 를 조회하는 전략  
초기에 DB(Data Store) 를 통해서 Cache Warming 작업이 필요하다. (캐시 관리의 주체 `어플리케이션`)

Cache Hit: `서버 -> 캐시(Hit) -> end`  
Cache Miss: `서버 -> 캐시(Miss) -> DB -> end`

- 장점: 캐시가 죽어도 어플리케이션에 장애가 나지 않음 (DB 에서 조회하면 됨)
- 단점:
  - 데이터 정합성이 맞지 않을 수 있음
  - 초기에 Cache Warming 이 없다면 서비스 초기에 조회가 몰려서 부하를 주게됨

```
사용 예시
ex1) 상품을 하루 기준으로 자정에 Cache Warming
  - 크게 변경이 없으면서 메인 페이지 같은 경우 유저가 무조건 호출하게 되어 있음
  - 가격이 갑자기 경우 Cache Clear 혹은 Eviction 이나 수정

ex2) 자주 구매하는 인기 상품 TTL 설정 30 ~ 1 시간 (현재 3일 집계)
  - 조회 빈도 높음
  - 크게 변하지 않으며 실제 DB 와 데이터가 다름이 큰 이슈는 아님(아니라고 생각함...)
  
ex3) 자주 조회되는 상품 캐싱
  - 상품에는 가격, 이름, 이미지, 브랜드, 옵션 등 데이터가 많이 있으니 검색된 빈도에 따라서 혹은 일정량 만큼 캐싱
```

> [!IMPORTANT]
> Cache Warming: 미리 Cache 에 데이터를 쌓아두는 작업

#### 2️⃣Read Through

어플리케이션이 캐시만 바라보도록 하는 전략 (캐시 관리의 주체 `캐시`)

- 장점: 데이터 동기화
- 단점: 데이터 동기화를 캐시
- 에게 위임하면서 캐시에 의존하게 되는 전략으로 캐시 서비스가 죽으면 운영 서비스도 장애 발생

```
사용 방법은 Cache Aside 와 크게 다를 것 같지 않음
- Cache Aside 처럼 변경이 잦지 않은 조회성 데이터에 좋을 것 같음
- 변경이 있는 경우 캐시 Eviction 을 통해서 의도적으로 Cache Miss 를 만들어서 변경된 데이터를 Caching 하는 전략도 괜찮을 듯 
- 오히려 캐시 서비스가 정지 되었을 때 대처가 추가로 필요하여(복제본 등) 안좋지 않을까...?
```

Cache Hit: `서버 -> 캐시(Hit) -> end`  
Cache Miss: `서버 -> 캐시(Miss) -> DB -> 캐시(Caching) -> end`

### 👉 캐시 쓰기 전략(Write Cache Strategy)

#### 1️⃣Write Back(Write Behind)

모든 데이터를 캐시(Cache Store) 에 저장하고, 일정 시간 후 DB 에 반영하는 전략

- 장점:
  - 캐시가 주체가 되어 DB 반영하기 때문에 데이터 정합성 확보
  - 일정 시간 데이터를 모아서 한 번에 질의하기 때문에 DB 에 쓰기 쿼리 비용 감소
- 단점:
  - 캐시 장애나면 데이터 영구 손실
  - 자주 사용되지 않는 불필요한 데이터도 캐싱됨 (일정 시간 동안)

```
사용 방법

- 좋아요, 조회수 같은 Write / Read 작업이 빈번하지만 손실되도 크리티컬하지 않은 데이터에 적합해보임 
```

#### 2️⃣ Write Through

Write 작업시 캐시와 DB 에 동시에 데이터를 저장하는 전략

- 장점:
  - 캐시와 DB가 항상 동기화 상태
  - 데이터 유실 x
- 단점:
  - 느리다 (DB I/O 까지 매번 이루어짐) -> 요청 1 = Write 2

```
사용 방법

- Read 작업은 많지만 Write 는적고, 또 한 번 업데이트 되면 데이터 손실이 없어야 하는 작업.... 뭔지 잘 모르겠음
  - 이커머스에 상품 관리 시스템에서 가격을 변경할 수 있다면 이런 전략을 사용할 것 같음
    - 상품들이 캐싱되어 있을 때 캐시와 함께 DB 도 반영 되어야 하니까
  - 멤버 객체를 Resolver 를 통해서 파라미터로 받는다면 매번 DB 를 조회하기 보다는 캐시에 멤버를 식별할 아이디 값과 데이터를 두고 사용하다가 변경이
    경우 Write Through 전략이 적합할 것 같음 (또 추가로 로그인 유지 시간 등 관리도 가능하니까)
```

---

## 📌캐시 스탬피드(Cache Stamped)

다수의 요청이 같은 캐시의 key 대한 데이터를 조회할 때 캐시 데이터가 TTL 가 만료되어 Cache Miss 가 발생할 때  
해당 요청들이 한 번에 DB 에 접근해서 동일한 Read 작업이 발생하고, DB 는 부하를 받게 되는 현상  
DB 에서 가져온 데이터를 다시 Caching 하는 과정에서도 중복되는 Write 작업이 발생한다.

---

임시 (Redis)

전체 데이터 출력  
`ZREVRANGE {key} 0 -1 WITHSCORES`

```
ZREVRANGE APPLY-COUPON-2 0 -1 WITHSCORES
1) "USER-2"
2) "1738597074429"
3) "USER-1"
4) "1738597005823"
```

순서 조회  
`ZRANK {key} {key}`

```
ZRANK APPLY-COUPON-2 USER-1
(integer) 0
```

제거  
`ZREM {key} {key}`

전체 삭제  
`DEL {key}`
