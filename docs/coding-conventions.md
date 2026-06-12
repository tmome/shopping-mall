# Coding Conventions

Prefer conventions from `/Users/hyunseung2/Desktop/repository/fitpetmall-backend-v4-main` when adding backend structure to this project. If a local choice conflicts with the mall project, mirror the mall project unless there is a clear reason not to.

## Package Structure

Feature packages should be split by role.

Use this shape when adding or moving code:

```text
com.example.shoppingmall.<feature>.controller
com.example.shoppingmall.<feature>.dto
com.example.shoppingmall.<feature>.service
com.example.shoppingmall.<feature>.service.model
com.example.shoppingmall.<feature>.domain
com.example.shoppingmall.<feature>.repository
```

Configuration can live in `config` under the related feature or in `common` when it is truly shared.

Shared API infrastructure should also be split by role. For example:

```text
com.example.shoppingmall.common.handler
com.example.shoppingmall.common.dto
com.example.shoppingmall.common.pagination
com.example.shoppingmall.common.querydsl
```

Paging responses should follow the mall project shape:

```kotlin
data class Pagination(
    val totalCount: Long,
    val totalPage: Int,
    val count: Int,
    val page: Int,
    val perPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
)
```

Use `Pagination.valueOf(page)` and wrap list responses as `PaginationImpl<T>(pagination, content)`.

## QueryDSL

Follow the mall project direction for QueryDSL.

Do not add custom repository interfaces only to make QueryDSL available. Prefer constructor injection of `JPAQueryFactory` into the class that owns the query, then call QueryDSL directly.

Use `BooleanExpression` helpers for dynamic conditions instead of `BooleanBuilder` when possible. If a condition method returns `null`, QueryDSL omits it from the `where` clause, which keeps query construction explicit.

For simple list paging, use shared query helpers such as `pageAwareFetch(pageable)` instead of repeating `offset(...).limit(...)` everywhere.

## Controller Layer

Controllers should only handle HTTP concerns:

- request/response DTOs
- validation annotations
- authentication principal extraction
- mapping request DTOs to service commands
- mapping service results to response DTOs

Do not define request or response `data class` types inside Controller files.

## Service Layer

Services should not depend on Controller API DTOs.

Use service-specific command/result models instead:

- Controller request DTO -> Service command
- Service result -> Controller response DTO

This keeps API shape changes from leaking into business logic.

## Kotlin Function Style

Prefer block body functions with explicit `return` in project code, especially in Controller and Service code.

Prefer this:

```kotlin
fun get(id: Long): ProductResponse {
    return productService.findById(id).toResponse()
}
```

Avoid this:

```kotlin
fun get(id: Long): ProductResponse =
    productService.findById(id).toResponse()
```
