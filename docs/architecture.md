# Architecture

```text
React Dashboard
      |
      v
API Gateway :8080 -- JWT validation, role checks, CORS, route forwarding
      |
      +--> auth-service :8081 -------- PostgreSQL auth_db
      +--> transaction-service :8082 - PostgreSQL transaction_db -- Kafka transaction events
      +--> fraud-detection-service :8083 -- MongoDB fraud_db -- Kafka fraud alerts
      +--> reconciliation-service :8084 -- PostgreSQL reconciliation_db -- Kafka completion events
      +--> notification-service :8085 -- PostgreSQL notification_db -- Kafka consumers
      +--> reporting-service :8086 -- dashboard/report APIs

Eureka discovery-server :8761 registers all backend services.
Kafka topics connect payment, fraud, reconciliation, and notification flows.
Redis is included for production-ready caching/session expansion.
Actuator Prometheus endpoints are exposed for Grafana dashboards.
```

The gateway enforces role rules for transaction, fraud, reconciliation, reporting, notification, and user-management APIs. It forwards `X-Authenticated-User`, `X-User-Id`, and `X-User-Roles` headers to downstream services after JWT validation.

The code follows controller, service, repository, DTO/entity boundaries. Common API responses, audit fields, exceptions, request correlation, and Kafka event DTOs live in `common-library`.
