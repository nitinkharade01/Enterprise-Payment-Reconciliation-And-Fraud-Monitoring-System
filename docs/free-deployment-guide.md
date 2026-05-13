# Free Deployment Guide

This guide prepares the project for a low-resource free deployment from GitHub.

Recommended first deployment:

- Backend: Render web services
- Database: Render PostgreSQL free database
- Frontend: Vercel
- Kafka: disabled
- Redis: disabled
- Eureka: disabled in production simple mode
- MongoDB: optional through MongoDB Atlas free tier, only needed if you deploy `fraud-detection-service`

## What Runs First

Deploy only the essential services first:

- `api-gateway`
- `auth-service`
- `transaction-service`
- `frontend`

Keep these optional until you have more RAM/CPU available:

- `discovery-server`
- `fraud-detection-service`
- `reconciliation-service`
- `notification-service`
- `reporting-service`

## GitHub Push

From the project root:

```bash
git status
git add .
git commit -m "Prepare project for free cloud deployment"
git push origin CFBP-120526
```

If your GitHub repository contains this project inside a nested folder named `enterprise-payment-reconciliation-fraud-monitoring`, set that folder as the root directory in Render and Vercel.

## Render Backend Deployment

### Option A: Blueprint

Use `render.yaml` to create:

- `epay-api-gateway`
- `epay-auth-service`
- `epay-transaction-service`
- `epay-postgres`

Render reads `DATABASE_URL` from the PostgreSQL database. The project includes a small adapter that converts Render's `postgresql://...` connection string into a Spring JDBC URL at startup.

During Blueprint setup, Render will ask for:

- `FRONTEND_URL`: your Vercel URL, for example `https://your-app.vercel.app`
- `PUBLIC_BASE_URL`: your Render gateway URL, for example `https://epay-api-gateway.onrender.com`

After the first deploy, update the gateway service variable:

```text
CORS_ALLOWED_ORIGIN_PATTERNS=https://your-app.vercel.app,https://*.vercel.app,https://*.netlify.app
```

### Option B: Manual Render Services

Create three Render Web Services from the same GitHub repository:

| Service | Dockerfile | Env `SERVICE_NAME` | Health check |
|---|---|---|---|
| API Gateway | `backend/Dockerfile` | `api-gateway` | `/actuator/health` |
| Auth Service | `backend/Dockerfile` | `auth-service` | `/actuator/health` |
| Transaction Service | `backend/Dockerfile` | `transaction-service` | `/actuator/health` |

Set these common variables on every backend service:

```text
SPRING_PROFILES_ACTIVE=prod
EUREKA_ENABLED=false
KAFKA_ENABLED=false
REDIS_ENABLED=false
```

Set these on `auth-service` and `transaction-service`:

```text
DATABASE_URL=<Render PostgreSQL internal connection string>
JWT_SECRET=<same long secret used by api-gateway and auth-service>
```

Set these on `api-gateway`:

```text
JWT_SECRET=<same long secret used by auth-service>
AUTH_SERVICE_URL=http://epay-auth-service:10000
TRANSACTION_SERVICE_URL=http://epay-transaction-service:10000
FRONTEND_URL=https://your-app.vercel.app
PUBLIC_BASE_URL=https://epay-api-gateway.onrender.com
CORS_ALLOWED_ORIGIN_PATTERNS=https://your-app.vercel.app,https://*.vercel.app,https://*.netlify.app
```

## PostgreSQL Setup

For Render PostgreSQL:

1. Create a PostgreSQL database.
2. Copy the internal connection string.
3. Set it as `DATABASE_URL` for `auth-service` and `transaction-service`.

The same free database can be used for the first deployment. Hibernate creates separate tables for each service.

Free-tier warning: Render PostgreSQL free databases are temporary and may expire after 30 days. Use this for demos and portfolio review, not long-term data.

## Vercel Frontend Deployment

In Vercel:

1. Import the GitHub repository.
2. Set the root directory to `frontend` if Vercel is opened from the project root.
3. Use the default Vite build:

```text
Build Command: npm run build
Output Directory: dist
Install Command: npm ci
```

4. Add this environment variable:

```text
VITE_API_BASE_URL=https://epay-api-gateway.onrender.com
```

Vite exposes client-side environment variables only when they start with `VITE_`.

## Testing Deployed APIs

Check gateway health:

```bash
curl https://epay-api-gateway.onrender.com/actuator/health
```

Login through the gateway:

```bash
curl -X POST https://epay-api-gateway.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "admin@payment.com", "password": "Admin@123" }'
```

Create a transaction:

```bash
curl -X POST https://epay-api-gateway.onrender.com/api/transactions \
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

## Optional Services

Deploy optional services later:

- `fraud-detection-service`: requires `MONGODB_URI`; Kafka can remain disabled for manual API demos.
- `reconciliation-service`: requires `DATABASE_URL`.
- `notification-service`: requires `DATABASE_URL`; event listeners stay disabled when `KAFKA_ENABLED=false`.
- `reporting-service`: can run without a database because current reporting APIs return sample aggregates.
- `discovery-server`: not required when `EUREKA_ENABLED=false`.

## Local Run

Full local Docker run:

```bash
docker compose -f docker/docker-compose.yml up --build
```

Essential local services only:

```bash
cd backend
mvn -pl common-library,api-gateway,auth-service,transaction-service -am test
```

Frontend:

```bash
cd frontend
npm ci
npm run dev
```

## Common Errors And Fixes

### Gateway returns 502

Check `AUTH_SERVICE_URL` and `TRANSACTION_SERVICE_URL`. In Render, these must point to the backend service URLs reachable from the gateway.

### Browser CORS error

Add the exact deployed frontend URL to:

```text
CORS_ALLOWED_ORIGIN_PATTERNS
```

Then redeploy `api-gateway`.

### Database connection fails

Use Render's internal PostgreSQL connection string in `DATABASE_URL`. The app accepts both `postgresql://...` and `jdbc:postgresql://...`.

### JWT is invalid between services

`api-gateway` and `auth-service` must use the same `JWT_SECRET`.

### Free service sleeps

Free web services can sleep after inactivity. The first request after sleep can be slow.

