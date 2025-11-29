# ADR 1: Layered Architecture

## Status
Accepted

## Context
The Café POS system has grown from a simple order processing application to include multiple patterns (Command, Adapter, Composite, Iterator, State, Observer) and needs a clear architectural structure. Without explicit layers, business logic was becoming entangled with UI concerns, making the system harder to test, maintain, and evolve.

## Decision
We will organize the codebase into a four-layer architecture:
- **Presentation Layer (UI)**: Handles I/O, console views, and controllers
- **Application Layer (App)**: Orchestrates use cases and coordinates domain operations
- **Domain Layer**: Contains business entities, value objects, and core business logic
- **Infrastructure Layer (Infra)**: Provides repositories, adapters, and external system integrations

Dependencies flow inward: UI → App → Domain, with Infrastructure as adapters that depend on Domain interfaces.

## Consequences

### Pros
- **Clear separation of concerns**: Business logic is independent of UI and infrastructure
- **Testability**: Domain logic can be tested without UI or database dependencies
- **Maintainability**: Changes to UI or infrastructure don't affect core business logic
- **Flexibility**: Easy to swap implementations (e.g., in-memory repository → database repository)
- **Scalability**: Natural boundaries for future service extraction

### Cons
- **Initial overhead**: Requires more upfront structure and wiring code
- **Indirection**: More layers can make code paths harder to trace
- **Learning curve**: Team members need to understand layer boundaries

## Implementation
- Domain interfaces (e.g., `OrderRepository`) defined in `com.cafepos.domain`
- Application services (e.g., `CheckoutService`) in `com.cafepos.app`
- Infrastructure implementations (e.g., `InMemoryOrderRepository`) in `com.cafepos.infra`
- Composition root (`Wiring`) centralizes dependency injection

