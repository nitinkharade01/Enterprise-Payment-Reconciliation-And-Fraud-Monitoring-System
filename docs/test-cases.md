# Test Cases

This file tracks the recovered manual and automated test coverage for the Enterprise Payment Reconciliation & Fraud Monitoring System.

## Test Commands

Run the recovered root Spring Boot tests:

```bash
mvn test
```

Run the recovered backend microservice tests:

```bash
cd backend
mvn test
```

Frontend source and package scripts have been restored. Run:

```bash
cd frontend
npm test
```

## Manual Functional Test Cases

| Test Case ID | Module | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|---|---|
| AUTH-001 | Auth service | Register user | Auth service running | POST `/api/auth/register` | Admin/finance/auditor payload | User is created with encoded password and role | Pending manual run | Not Run |
| AUTH-002 | Auth service | Login user | Seed user exists | POST `/api/auth/login` | `admin@payment.com` / `Admin@123` | JWT token returned | Pending manual run | Not Run |
| AUTH-003 | Auth service | Profile endpoint | Valid JWT | GET `/api/auth/profile` | Bearer token | Current user profile returned | Pending manual run | Not Run |
| GW-001 | API Gateway | Public auth route | Gateway running | POST `/api/auth/login` through gateway | Login JSON | Request is routed without JWT | Pending manual run | Not Run |
| GW-002 | API Gateway | Protected route without token | Gateway running | GET `/api/transactions` | None | `401 Unauthorized` | Pending manual run | Not Run |
| GW-003 | API Gateway | Role mismatch | Valid auditor JWT | POST `/api/transactions` | Auditor token | `403 Forbidden` | Pending manual run | Not Run |
| TXN-001 | Transaction service | Create transaction | Finance token | POST `/api/transactions` | Valid transaction JSON | Transaction stored and event publisher invoked | Covered by unit test | Pass |
| TXN-002 | Transaction service | Update status | Existing transaction | PUT `/api/transactions/{id}/status` | `SUCCESS` | Audit log created and status updated | Covered by unit test | Pass |
| FRAUD-001 | Fraud service | High-value fraud rule | Transaction event available | Evaluate amount above threshold | Amount `76000` | `HIGH_RISK` decision | Covered by unit test | Pass |
| FRAUD-002 | Fraud service | List alerts | Alerts exist | GET `/api/fraud-alerts` | None | Alert list returned | Covered by controller test | Pass |
| RECON-001 | Reconciliation service | Upload CSV | CSV file available | POST `/api/reconciliation/upload` | Settlement CSV | Summary created and completion event invoked | Covered by unit test | Pass |
| RECON-002 | Reconciliation service | Download report | Existing file id | GET `/api/reconciliation/download/{fileId}` | `fileId=10` | CSV report returned | Pending manual run | Not Run |
| NOTIF-001 | Notification service | Record fraud notification | Fraud event available | Consume/record fraud event | Fraud alert event | Notification row stored | Covered by unit test | Pass |
| NOTIF-002 | Notification service | Mark read | Notification exists | PUT `/api/notifications/{id}/read` | `id=1` | Notification read flag set | Covered by unit test | Pass |
| REPORT-001 | Reporting service | Dashboard metrics | Reporting service running | GET `/api/reports/dashboard` | None | Dashboard payload returned | Covered by unit test | Pass |
| FE-001 | React frontend | Test harness loads | Frontend dependencies restored | Run frontend tests | Vitest | Test harness executes | Covered by frontend smoke test | Pass |
| SEC-001 | Security/JWT | Generate JWT | User exists | Generate token | User roles | Signed token returned | Covered by unit test | Pass |
| LOCAL-001 | Local setup | Kafka disabled startup | `app.kafka.enabled=false` | Start services | Local profile | Services do not require Kafka | Covered by no-op bean tests where restored | Pass |
| DB-001 | Database | H2 test profile | Maven test | Run backend tests | `application-test.yml` | Tests do not need a local MySQL instance | Covered by test profile | Pass |
