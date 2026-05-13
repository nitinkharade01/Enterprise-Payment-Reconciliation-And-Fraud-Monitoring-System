# API Documentation

Base URL through gateway: `http://localhost:8080`

## Auth
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/profile`
- `GET /api/auth/users`

Login request:
```json
{ "email": "admin@payment.com", "password": "Admin@123" }
```

## Transactions
- `POST /api/transactions`
- `GET /api/transactions?page=0&size=20&sort=createdAt,desc`
- `GET /api/transactions/{transactionId}`
- `GET /api/transactions/status/{status}`
- `PUT /api/transactions/{transactionId}/status`
- `GET /api/transactions/search?keyword=Aarav`

Create request:
```json
{
  "userId": 2,
  "customerName": "Aarav Mehta",
  "sourceAccount": "ACC-1001",
  "destinationAccount": "ACC-9001",
  "amount": 76000,
  "currency": "INR",
  "paymentMode": "UPI",
  "remarks": "Invoice payment"
}
```

## Fraud
- `GET /api/fraud-alerts`
- `GET /api/fraud-alerts/{id}`
- `PUT /api/fraud-alerts/{id}/status?status=CLOSED`

## Reconciliation
- `POST /api/reconciliation/upload`
- `GET /api/reconciliation/summary/{fileId}`
- `GET /api/reconciliation/mismatches/{fileId}`
- `GET /api/reconciliation/download/{fileId}`

## Reports
- `GET /api/reports/dashboard`
- `GET /api/reports/transactions/daily`
- `GET /api/reports/payment-mode-distribution`
- `GET /api/reports/fraud-risk-summary`

## Notifications
- `GET /api/notifications`
- `PUT /api/notifications/{id}/read`
