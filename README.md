# Enterprise Payment Reconciliation & Fraud Monitoring System

An industry-style Java full stack microservices project for processing payment transactions, detecting suspicious activity, reconciling bank or gateway settlement files, and giving finance/admin teams a dashboard for monitoring, reports, alerts, notifications, and audit visibility.

This project is designed to represent the kind of real-world work expected from a Java Full Stack Developer with around 3.6 years of experience: Spring Boot microservices, secure APIs, Kafka-based asynchronous processing, relational and document databases, React dashboard, Dockerized local deployment, and CI/CD readiness.

## Business Problem



Payment teams need to process large numbers of customer transactions while continuously answering three operational questions:

- Was the payment successful and traceable?
- Does the internal transaction match the bank or gateway settlement file?
- Is the transaction pattern suspicious enough to require review?

In many enterprise systems these workflows are spread across manual spreadsheets, delayed fraud checks, disconnected settlement reports, and limited audit trails. This increases operational risk, slows reconciliation, and makes fraud monitoring reactive instead of proactive.

## Solution

The platform provides a centralized payment operations system with:

- Secure user authentication and role-based access.
- Transaction creation, search, status lifecycle, and audit logging.
- Kafka event publishing for transaction and reconciliation events.
- Rule-based fraud detection using asynchronous Kafka consumers.
- CSV/XLSX settlement file upload and reconciliation result generation.
- Notification logging for payment, fraud, and reconciliation events.
- Dashboard APIs and React visualizations for operational reporting.
- Docker Compose setup for local full-stack execution.

## Architecture

```text
                              +------------------+
                              |   React Frontend |
                              |   localhost:5173 |
                              +---------+--------+
                                        |
                                        v
                              +------------------+
                              |   API Gateway    |
                              |   localhost:8080 |
                              | JWT + RBAC + CORS|
                              +---------+--------+
                                        |
            +---------------------------+----------------------------+
            |                           |                            |
            v                           v                            v
  +------------------+       +---------------------+       +---------------------+
  |   auth-service   |       | transaction-service |       | reporting-service   |
  | MySQL            |       | MySQL + Kafka       |       | Dashboard APIs      |
  +------------------+       +----------+----------+       +---------------------+
                                         |
                                         v
                                Kafka Topics
                                         |
            +----------------------------+----------------------------+
            |                            |                            |
            v                            v                            v
 +----------------------+     +----------------------+      +----------------------+
 | fraud-detection      |     | reconciliation       |      | notification         |
 | MongoDB + Kafka      |     | MySQL + Kafka        |      | MySQL + Kafka        |
 +----------------------+     +----------------------+      +----------------------+

                           +------------------+
                           | discovery-server |
                           | Eureka :8761     |
                           +------------------+
```

All backend services register with Eureka. The API Gateway is the public backend entry point and forwards requests to services through service discovery.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3.x, Spring Cloud 2023.x |
| Microservices | Spring Cloud Gateway, Netflix Eureka |
| Security | Spring Security, JWT, BCrypt, role-based access control |
| Messaging | Apache Kafka, Zookeeper |
| Databases | MySQL, MongoDB |
| Cache/Infra Ready | Redis |
| Frontend | React.js, Axios, React Router, Recharts |
| Validation/Docs | Jakarta Validation, Swagger/OpenAPI-ready dependencies |
| Monitoring | Spring Boot Actuator, Prometheus registry |
| Testing | JUnit 5, Mockito/Spring Boot Test setup |
| DevOps | Docker, Docker Compose, Jenkinsfile |

## Microservices

| Service | Port | Responsibility |
|---|---:|---|
| `discovery-server` | `8761` | Eureka service registry |
| `api-gateway` | `8080` | Central routing, CORS, JWT validation, role enforcement |
| `auth-service` | `8081` | Registration, login, JWT generation, user and role management |
| `transaction-service` | `8082` | Payment transaction lifecycle, search, pagination, Kafka event publishing |
| `fraud-detection-service` | `8083` | Kafka consumer, fraud rule evaluation, fraud alert storage |
| `reconciliation-service` | `8084` | Settlement file upload, CSV/XLSX parsing, reconciliation summary/report |
| `notification-service` | `8085` | Kafka consumer and mock notification log storage |
| `reporting-service` | `8086` | Dashboard and reporting APIs |

In Docker Compose, business services are exposed internally and accessed through the API Gateway.

## Kafka Topics

| Topic | Producer | Consumer |
|---|---|---|
| `transaction-created-topic` | transaction-service | fraud-detection-service |
| `transaction-status-updated-topic` | transaction-service | notification-service |
| `fraud-alert-topic` | fraud-detection-service | notification-service |
| `reconciliation-completed-topic` | reconciliation-service | notification-service |
| `notification-topic` | Reserved for notification fan-out | notification-service |

Shared event DTOs are stored in `backend/common-library`.

## Database Design

### auth-service MySQL

- `users`
- `roles`
- `user_roles`

Stores application users, encoded passwords, and assigned roles.

### transaction-service MySQL

- `transactions`
- `transaction_audit_logs`

Stores payment transactions, lifecycle status, risk status, reconciliation status, and status-change audit events.

### fraud-detection-service MongoDB

- `fraud_alerts`
- `fraud_rules`

Stores flexible fraud alert documents and rule metadata.

### reconciliation-service MySQL

- `settlement_files`
- `reconciliation_records`
- `reconciliation_summary`

Stores uploaded settlement file metadata, record-level match results, and summary counts.

### notification-service MySQL

- `notifications`

Stores mock notification logs for payment status, fraud alert, and reconciliation completion events.

## API Endpoints

Base URL:

```text
http://localhost:8080
```

### Auth APIs

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/api/auth/register` | Public |
| `POST` | `/api/auth/login` | Public |
| `GET` | `/api/auth/profile` | Authenticated |
| `GET` | `/api/auth/users` | `ADMIN` |

### Transaction APIs

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/api/transactions` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/transactions` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/transactions/{transactionId}` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/transactions/status/{status}` | `ADMIN`, `FINANCE_USER` |
| `PUT` | `/api/transactions/{transactionId}/status` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/transactions/search?keyword=value` | `ADMIN`, `FINANCE_USER` |

### Fraud APIs

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/fraud-alerts` | `ADMIN`, `AUDITOR` |
| `GET` | `/api/fraud-alerts/{id}` | `ADMIN`, `AUDITOR` |
| `PUT` | `/api/fraud-alerts/{id}/status?status=CLOSED` | `ADMIN`, `AUDITOR` |

### Reconciliation APIs

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/api/reconciliation/upload` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/reconciliation/summary/{fileId}` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/reconciliation/mismatches/{fileId}` | `ADMIN`, `FINANCE_USER` |
| `GET` | `/api/reconciliation/download/{fileId}` | `ADMIN`, `FINANCE_USER` |

### Reporting APIs

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/reports/dashboard` | `ADMIN`, `AUDITOR` |
| `GET` | `/api/reports/transactions/daily` | `ADMIN`, `AUDITOR` |
| `GET` | `/api/reports/payment-mode-distribution` | `ADMIN`, `AUDITOR` |
| `GET` | `/api/reports/fraud-risk-summary` | `ADMIN`, `AUDITOR` |

### Notification APIs

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/notifications` | Authenticated roles |
| `PUT` | `/api/notifications/{id}/read` | Authenticated roles |

## Security Flow

1. User logs in through `auth-service`.
2. Password is verified using BCrypt.
3. `auth-service` generates a signed JWT containing user identity and roles.
4. React stores the JWT and sends it in the `Authorization` header.
5. API Gateway validates the JWT signature and expiry.
6. API Gateway enforces role-based authorization by route group.
7. API Gateway forwards authenticated identity headers to downstream services:
   - `X-Authenticated-User`
   - `X-User-Id`
   - `X-User-Roles`

Public APIs:

- `/api/auth/login`
- `/api/auth/register`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/actuator/**`

## Frontend Features

- Login page
- Register user page
- Protected routes
- Role-based navigation
- Dashboard metrics
- Daily transaction volume chart
- Payment mode distribution chart
- Fraud risk summary
- Recent transactions table
- Transaction list and details pages
- Create transaction page
- Fraud alerts page
- Reconciliation upload and result page
- Authenticated reconciliation report download
- Reports page
- User management page
- Audit logs page placeholder

## Docker Setup

Docker Compose starts:

- MySQL
- MongoDB
- Redis
- Zookeeper
- Kafka
- Eureka discovery server
- API Gateway
- Auth service
- Transaction service
- Fraud detection service
- Reconciliation service
- Notification service
- Reporting service
- React frontend

Docker Compose builds backend service images from the Maven multi-module backend. Local `target/` folders are not required before running Compose.

```bash
docker compose -f docker/docker-compose.yml up --build
```

Stop containers:

```bash
docker compose -f docker/docker-compose.yml down
```

## Free Cloud Deployment

The project now supports a low-resource production mode for free cloud platforms.

Recommended first deployment:

- Render: `api-gateway`, `auth-service`, `transaction-service`, and PostgreSQL.
- Vercel: React frontend.
- Disabled by default: Kafka, Redis, and Eureka.
- Optional: MongoDB Atlas for `fraud-detection-service`.

Production mode is enabled with:

```text
SPRING_PROFILES_ACTIVE=prod
KAFKA_ENABLED=false
REDIS_ENABLED=false
EUREKA_ENABLED=false
```

The gateway can route directly to backend service URLs when Eureka is disabled:

```text
AUTH_SERVICE_URL=https://your-auth-service.onrender.com
TRANSACTION_SERVICE_URL=https://your-transaction-service.onrender.com
```

The frontend reads its backend URL from:

```text
VITE_API_BASE_URL=https://your-api-gateway.onrender.com
```

Environment examples are included in `.env.example` and `frontend/.env.example`.

Free deployment limitations:

- Free web services can sleep after inactivity.
- Free PostgreSQL storage and lifetime are limited; use it for demos, not permanent data.
- Running every microservice separately can exceed free CPU/RAM limits.
- Kafka, Redis, Eureka, and MongoDB should stay disabled or undeployed until needed.

Detailed deployment steps are in `docs/free-deployment-guide.md`.

## Jenkins Pipeline

The included `Jenkinsfile` contains the following CI/CD stages:

1. Checkout
2. Build
3. Run Tests
4. SonarQube Analysis placeholder
5. Build Docker Images
6. Push Docker Images placeholder
7. Deploy placeholder

This structure is ready to extend for AWS ECS, EKS, or EC2-based deployments.

## How To Run Locally

### Prerequisites

- Java 17
- Maven 3.9+
- Node.js 20+
- MySQL 8+ with username `root` and password `root123`
- Docker Desktop or Docker Engine

### Run With Docker Compose

```bash
git clone <repository-url>
cd enterprise-payment-reconciliation-fraud-monitoring
docker compose -f docker/docker-compose.yml up --build
```

Open:

```text
Frontend: http://localhost:5173
Gateway:  http://localhost:8080
Eureka:   http://localhost:8761
```

### Run Backend Tests

```bash
cd backend
mvn test
```

### MySQL Configuration

The JPA-backed services use MySQL locally with these defaults:

```text
Username: root
Password: root123
Host:     localhost:3306
```

Default service databases are created automatically when the MySQL user has permission:

```text
payment_auth_db
payment_transaction_db
payment_reconciliation_db
payment_notification_db
```

### Run Frontend Only

```bash
cd frontend
npm install
npm run dev
```

## Sample Credentials

| Role | Email | Password |
|---|---|---|
| `ADMIN` | `admin@payment.com` | `Admin@123` |
| `FINANCE_USER` | `finance@payment.com` | `Finance@123` |
| `AUDITOR` | `auditor@payment.com` | `Auditor@123` |

## Sample Requests

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "admin@payment.com", "password": "Admin@123" }'
```

### Create Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "customerName": "Aarav Mehta",
    "sourceAccount": "ACC-1001",
    "destinationAccount": "ACC-9001",
    "amount": 76000,
    "currency": "INR",
    "paymentMode": "UPI",
    "remarks": "Invoice payment"
  }'
```

### Upload Reconciliation File

```bash
curl -X POST http://localhost:8080/api/reconciliation/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@docs/sample-settlement.csv"
```

## Screenshots

Add screenshots here after running the application:

```text
docs/screenshots/login.png
docs/screenshots/dashboard.png
docs/screenshots/transactions.png
docs/screenshots/fraud-alerts.png
docs/screenshots/reconciliation.png
docs/screenshots/reports.png
```

## Interview Explanation

This project can be explained as an enterprise payment operations platform built with Java 17, Spring Boot 3, Spring Cloud microservices, Kafka, MySQL, MongoDB, and React.

The main workflow starts when a finance user creates a payment transaction. The transaction service stores the transaction in MySQL and publishes a Kafka event. Fraud detection consumes the event asynchronously, applies rule-based checks, stores alerts in MongoDB, and publishes fraud alert events. Reconciliation service allows finance users to upload bank or gateway settlement files, parses CSV/XLSX data, identifies matched, mismatched, missing, and duplicate records, stores summaries, and publishes completion events. Notification service consumes important events and stores mock notification logs.

Security is handled centrally at the API Gateway. Auth service validates credentials and issues JWT tokens. The gateway validates every protected request and enforces role-based access for admin, finance, and auditor users.

Microservices were used because each domain has a different scaling and ownership pattern. Transaction processing, fraud checks, reconciliation file processing, reporting, and notifications can evolve independently. Kafka decouples real-time workflows and prevents one service from blocking another.

## Future Enhancements

- Add Testcontainers for MySQL, MongoDB, and Kafka integration tests.
- Add Kafka retry topics and dead-letter topics.
- Add Schema Registry for event contract compatibility.
- Replace mock notifications with real email/SMS provider integration.
- Implement live reporting aggregation from service databases or event streams.
- Add Redis caching for dashboard summaries.
- Add OpenTelemetry distributed tracing.
- Add centralized logging with ELK or Loki.
- Add Terraform for AWS ECS/EKS deployment.
- Move secrets to AWS Secrets Manager or HashiCorp Vault.
- Add refresh-token flow and token revocation.
- Add reconciliation matching against live internal transaction APIs.

## Additional Documentation

- `docs/architecture.md`
- `docs/api-documentation.md`
- `docs/database-schema.md`
- `docs/kafka-events.md`
- `docs/free-deployment-guide.md`
- `docs/interview-explanation.md`
- `docs/postman-collection.json`
