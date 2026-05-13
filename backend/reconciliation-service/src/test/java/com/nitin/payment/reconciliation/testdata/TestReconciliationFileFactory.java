package com.nitin.payment.reconciliation.testdata;

import com.nitin.payment.reconciliation.entity.ReconciliationRecord;
import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import com.nitin.payment.reconciliation.entity.SettlementFile;
import java.math.BigDecimal;
import org.springframework.mock.web.MockMultipartFile;

public final class TestReconciliationFileFactory {

    private TestReconciliationFileFactory() {
    }

    public static MockMultipartFile csvFile() {
        String csv = "internalTransactionId,bankTransactionId,amount,status\nTXN-1,BANK-1,1000.00,SUCCESS\nTXN-2,BANK-2,1200.00,FAILED\n";
        return new MockMultipartFile("file", "settlement.csv", "text/csv", csv.getBytes());
    }

    public static SettlementFile settlementFile() {
        SettlementFile file = new SettlementFile();
        file.setId(10L);
        file.setOriginalFileName("settlement.csv");
        file.setFileType("text/csv");
        file.setStatus("PROCESSING");
        return file;
    }

    public static ReconciliationSummary summary() {
        ReconciliationSummary summary = new ReconciliationSummary();
        summary.setFileId(10L);
        summary.setTotalRecords(2);
        summary.setMatchedCount(1);
        summary.setMismatchedCount(1);
        return summary;
    }

    public static ReconciliationRecord mismatch() {
        ReconciliationRecord record = new ReconciliationRecord();
        record.setFileId(10L);
        record.setInternalTransactionId("TXN-2");
        record.setBankTransactionId("BANK-2");
        record.setInternalAmount(new BigDecimal("1200.00"));
        record.setBankAmount(new BigDecimal("1200.00"));
        record.setBankStatus("FAILED");
        record.setReconciliationStatus("MISMATCHED");
        return record;
    }
}
