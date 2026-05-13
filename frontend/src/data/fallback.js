export const fallbackDashboard = {
  totalTransactions: 1250,
  successCount: 1040,
  failedCount: 86,
  fraudCount: 18,
  reconciliationPendingCount: 124,
  dailyVolume: [
    { date: '2026-05-08', volume: 120000 },
    { date: '2026-05-09', volume: 155000 },
    { date: '2026-05-10', volume: 98000 },
    { date: '2026-05-11', volume: 210000 },
    { date: '2026-05-12', volume: 175000 }
  ],
  paymentModeDistribution: { UPI: 52, CARD: 23, NEFT: 15, IMPS: 10 },
  fraudRisk: { LOW_RISK: 980, MEDIUM_RISK: 42, HIGH_RISK: 18, BLOCKED: 4 },
  recentTransactions: [
    { transactionId: 'TXN-SAMPLE-001', customerName: 'Aarav Mehta', amount: 1520, status: 'SUCCESS' },
    { transactionId: 'TXN-SAMPLE-003', customerName: 'Rohan Iyer', amount: 76000, status: 'SUSPICIOUS' }
  ]
};

export const fallbackTransactions = [
  { transactionId: 'TXN-SAMPLE-001', customerName: 'Aarav Mehta', amount: 1520, currency: 'INR', paymentMode: 'UPI', status: 'SUCCESS', riskStatus: 'LOW_RISK', reconciliationStatus: 'MATCHED' },
  { transactionId: 'TXN-SAMPLE-002', customerName: 'Diya Shah', amount: 9400, currency: 'INR', paymentMode: 'CARD', status: 'PENDING', riskStatus: 'LOW_RISK', reconciliationStatus: 'PENDING' },
  { transactionId: 'TXN-SAMPLE-003', customerName: 'Rohan Iyer', amount: 76000, currency: 'INR', paymentMode: 'NEFT', status: 'SUSPICIOUS', riskStatus: 'HIGH_RISK', reconciliationStatus: 'MISMATCHED' }
];

export const fallbackAlerts = [
  { id: 'FRAUD-101', transactionId: 'TXN-SAMPLE-003', customerName: 'Rohan Iyer', severity: 'HIGH', status: 'OPEN', reason: 'High-value transaction above rule threshold' },
  { id: 'FRAUD-102', transactionId: 'TXN-SAMPLE-008', customerName: 'Neha Rao', severity: 'MEDIUM', status: 'REVIEW', reason: 'Repeated payments in short interval' }
];

export const fallbackNotifications = [
  { id: 1, title: 'Fraud alert created', message: 'High-risk alert for TXN-SAMPLE-003', read: false, type: 'FRAUD' },
  { id: 2, title: 'Reconciliation completed', message: 'Settlement batch processed with 2 mismatches', read: true, type: 'RECONCILIATION' }
];
