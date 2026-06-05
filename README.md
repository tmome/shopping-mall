# 하고 싶은대로 마음대로 해보는 쇼핑몰 API

Spring Boot, Kotlin, MySQL, Kafka, Spring Batch 기반의 간단한 쇼핑몰 API 프로젝트입니다.

## Requirements

- JDK 21
- Docker
- IntelliJ IDEA

## Run

로컬 인프라는 Docker Compose로 띄웁니다.

```bash
docker compose up -d
```

기본 포트는 아래와 같습니다.

| Service | URL |
| --- | --- |
| App | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| MySQL | localhost:3306 |
| Kafka | localhost:9092 |
| Kafka UI | http://localhost:8081 |

MySQL 계정은 `shopping / root`이고 DB 이름은 `shopping_mall`입니다.

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

브라우저에서 기본 상품 관리 화면을 열 수 있습니다.

```bash
open http://localhost:8080/
```

기본 DB 설정은 환경 변수로 바꿀 수 있습니다. 아래 예시는 로컬에 직접 떠 있는 MySQL을 쓸 때의 형태입니다.

```bash
DB_URL='jdbc:mysql://localhost:3306/shopping_mall?serverTimezone=Asia/Seoul&characterEncoding=UTF-8' \
DB_USERNAME=root \
DB_PASSWORD=your-password \
./gradlew bootRun
```

Kafka bootstrap server도 환경 변수로 바꿀 수 있습니다.

```bash
KAFKA_BOOTSTRAP_SERVERS=localhost:9092 SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

## API

### Create product

```bash
curl -X POST http://localhost:8080/api/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Keyboard","price":99000,"stockQuantity":10}'
```

### List products

```bash
curl http://localhost:8080/api/products
```

### Create order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"buyerName":"Kim","items":[{"productId":1,"quantity":2}]}'
```

### Get order

```bash
curl http://localhost:8080/api/orders/1
```

## Batch

Spring Batch 기본 구조는 `com.example.shoppingmall.batch` 패키지에 둡니다.

- 서버 시작 시 Job은 자동 실행하지 않습니다.
- Batch 인프라 설정은 `BatchConfig`에서 관리합니다.
- Batch 메타 테이블은 `db/batch-schema-mysql.sql`로 관리합니다.
- 실제 배치 작업은 `JobRepository`, `JobBuilder`, `StepBuilder` 기반의 `JobConfig`를 추가하면 됩니다.

## Notes

- 주문 생성 시 상품 row에 pessimistic lock을 걸고 재고를 차감합니다.
  - lock 방법은 트래픽에 따라 고려 대상이 될 수도 있음.
- JPA Auditing을 켜두어서 엔티티에 `createdAt`, `updatedAt`이 자동 기록됩니다.
- 결제, 쿠폰, 배송, Redis 같은 기능은 아직 넣지 않았습니다.
- 초기 개발 편의를 위해 `spring.jpa.hibernate.ddl-auto=update`를 사용합니다.
- API 문서는 springdoc-openapi로 자동 생성합니다.
- Batch Job 자동 실행은 `spring.batch.job.enabled=false`로 꺼둡니다.
