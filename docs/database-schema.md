# Database Schema

## auth-service
- `users`: id, full_name, email, password, active, created_at, updated_at, created_by, updated_by
- `roles`: id, name
- `user_roles`: user_id, role_id

## transaction-service
- `transactions`: id, transaction_id, user_id, customer_name, source_account, destination_account, amount, currency, payment_mode, transaction_status, risk_status, reconciliation_status, remarks, audit fields
- `transaction_audit_logs`: id, transaction_id, action, old_value, new_value, changed_by, created_at

## fraud-detection-service
- Mongo `fraud_alerts`: id, transactionId, userId, riskScore, riskStatus, fraudReason, alertStatus, createdAt
- Mongo `fraud_rules`: id, name, expression, active

## reconciliation-service
- `settlement_files`: id, original_file_name, file_type, status, audit fields
- `reconciliation_records`: id, internal_transaction_id, bank_transaction_id, internal_amount, bank_amount, internal_status, bank_status, reconciliation_status, mismatch_reason, file_id, created_at
- `reconciliation_summary`: id, file_id, total_records, matched_count, mismatched_count, missing_count, duplicate_count

## notification-service
- `notifications`: id, recipient, type, subject, message, read, audit fields
