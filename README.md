# Getting Started

포인트 지급, 회수, 사용, 취소의 기능을 하는 API 서비스   
Hexagonal 패턴을 적용하여 도메인 로직에서 IN/OUT을 담당하는 로직을 분리하도록 패키지를 구성함

### 패키지 소개   
- application   
Controller, Db 접금의 외부 서비스와의 통신을 담당하는 패키지
- common   
패키기 전반에 걸쳐서 사용하는 클래스들이 있는 패키지
- config   
캐시, QueryDSL 등의 특정 서비스의 환경 설정을 담당하는 패키지
- data   
DB 에서 데이터를 조회하거나 기록하기 위한 클래스들의 패키지
- domain   
application 패키지, data 패키지로부터 받은 데이터를 활용하여 각각의 use case를 위한 도메인의 비즈니스 로직을 포함하는 패키지

  
### 주요 기능
#### Controller   
[PointApiController](./src/main/java/com/point/system/application/port/input/controller/PointApiController.java) 에 포인트 지급, 회수, 사용, 취소에 대한 api 구현했으며 트래픽 제어를 위해서   
스레드 풀 방식의 벌크 헤드 적용함. 
- 지급: POST /api/v1/point
- 회수: POST /api/v1/point/retrieve
- 사용: POST /api/v1/point/order
- 취소: POST /api/v1/point/cancel

#### 포인트 정책   
포인트 지급 시에는 point_type_policy, point_lifespan_policy 테이블을 참고함. 해당 테이블은 데이터의 양이 적으므로   
별도의 index는 고려하지 않은 대신에 스케줄러를 사용하여 주기적으로 캐싱하도록 개발함   
[PointPolicyDbCacheScheduler](./src/main/java/com/point/system/application/PointPolicyDbCacheScheduler.java)    
[PointPolicyDbCacheService](./src/main/java/com/point/system/application/PointPolicyDbCacheService.java) 

#### 도메인 
도메인에 대한 비즈니스 로직은 [PointDomainService](./src/main/java/com/point/system/domain/PointDomainService.java)
과 [entities](./src/main/java/com/point/system/domain/entity)에 구현함

#### 실행방법
별도의 도커 설정은 없으며 spring에서 제공하는 embedded tomcat을 사용함

