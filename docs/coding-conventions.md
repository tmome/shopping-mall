# Coding Conventions

## Package Structure

Feature packages should be split by role.

Use this shape when adding or moving code:

```text
com.example.shoppingmall.<feature>.controller
com.example.shoppingmall.<feature>.dto
com.example.shoppingmall.<feature>.service
com.example.shoppingmall.<feature>.domain
com.example.shoppingmall.<feature>.repository
```

Configuration can live in `config` under the related feature or in `common` when it is truly shared.

Shared API infrastructure should also be split by role. For example:

```text
com.example.shoppingmall.common.handler
com.example.shoppingmall.common.dto
```

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
