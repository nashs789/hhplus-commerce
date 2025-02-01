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

※ RAM 에서 프로세스가 참조한 메모리를 못 찾게 된다면 Page Fault 발생으로 인터럽트와 함께 페이지 교체 혹은 로드까지의 과정도 있음

ex2) Request -> Web Server -> Redis -> -> Web Server -> DB -> Web Server -> Redis & Response
                              (Miss)                                       (Caching)
```
