# ADR 2: Event Bus for Component Communication

## Status
Accepted

## Context
As the system grows, components need to communicate without tight coupling. Direct method calls between Presentation and Application layers create rigid dependencies that make the system harder to evolve. We need a mechanism for components to publish and subscribe to events without knowing about each other.

## Decision
We will implement an in-process EventBus as a lightweight connector for component communication. Components can publish events (e.g., `OrderCreated`, `OrderPaid`) and subscribe to events they care about, without direct dependencies.

## Alternatives Considered

1. **Direct method calls**: Simple but creates tight coupling
2. **Observer pattern**: Already used for domain events, but requires explicit registration
3. **Message queue (RabbitMQ/Kafka)**: Overkill for current scale, adds infrastructure complexity
4. **In-process EventBus**: Lightweight, decoupled, easy to replace with distributed bus later

## Consequences

### Pros
- **Decoupling**: Components don't need direct references to each other
- **Extensibility**: New subscribers can be added without modifying publishers
- **Testability**: Easy to mock or verify event emissions
- **Evolution path**: Can be replaced with distributed message broker (Kafka, RabbitMQ) without changing event contracts
- **Flexibility**: Supports multiple subscribers for the same event

### Cons
- **Debugging**: Event flow can be harder to trace than direct calls
- **Type safety**: Requires runtime type checking (mitigated with sealed interfaces)
- **Synchronous by default**: Current implementation is synchronous; async would require additional complexity

## Implementation
- Event types defined as sealed interfaces in `com.cafepos.app.events`
- `EventBus` provides `on()` for subscription and `emit()` for publishing
- Used in `EventWiringDemo` to show UI reacting to application events

