# Shopping Mall API

Spring Boot, Kotlin, MySQL 기반의 간단한 쇼핑몰 API 프로젝트입니다.

## Requirements

- JDK 21
- MySQL 8.x
- IntelliJ IDEA

## Run

로컬 MySQL은 Docker Compose로 별도 컨테이너를 띄웁니다.

```bash
docker compose up -d mysql
```

`shopping-mall-mysql`은 호스트의 `3307` 포트에 연결됩니다. 기존 로컬 MySQL이나 다른 프로젝트의 MySQL이 `3306`을 써도 충돌하지 않습니다.

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

로컬 MySQL에는 먼저 DB를 만들어두면 됩니다.

```sql
CREATE DATABASE shopping_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
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

## Notes

- 주문 생성 시 상품 row에 pessimistic lock을 걸고 재고를 차감합니다.
  - lock 방법은 트래픽에 따라 고려 대상이 될 수도 있음.
- JPA Auditing을 켜두어서 엔티티에 `createdAt`, `updatedAt`이 자동 기록됩니다.
- 결제, 쿠폰, 배송, Redis, Kafka 같은 미들웨어성 기능은 아직 넣지 않았습니다.
- 초기 개발 편의를 위해 `spring.jpa.hibernate.ddl-auto=update`를 사용합니다.
