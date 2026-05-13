# Kafka Events

Topics:
- `transaction-created-topic`
- `transaction-status-updated-topic`
- `fraud-alert-topic`
- `reconciliation-completed-topic`
- `notification-topic`

Event DTOs are in `backend/common-library/src/main/java/com/nitin/payment/common/event`.

Flow:
1. Transaction service publishes `TransactionCreatedEvent`.
2. Fraud detection service consumes the transaction event and applies risk rules.
3. Fraud service publishes `FraudAlertEvent` for medium and high risk payments.
4. Notification service consumes transaction status, fraud alert, and reconciliation completion events.
5. Reconciliation service publishes `ReconciliationCompletedEvent` after file processing.
