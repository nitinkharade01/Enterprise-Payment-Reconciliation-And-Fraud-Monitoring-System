/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.exception.FileProcessingException
 *  com.nitin.payment.common.exception.ResourceNotFoundException
 *  lombok.Generated
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.Row
 *  org.apache.poi.ss.usermodel.Sheet
 *  org.apache.poi.ss.usermodel.Workbook
 *  org.apache.poi.ss.usermodel.WorkbookFactory
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.nitin.payment.reconciliation.service;

import com.nitin.payment.common.exception.FileProcessingException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.reconciliation.entity.ReconciliationRecord;
import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import com.nitin.payment.reconciliation.entity.SettlementFile;
import com.nitin.payment.reconciliation.messaging.ReconciliationEventPublisher;
import com.nitin.payment.reconciliation.repository.ReconciliationRecordRepository;
import com.nitin.payment.reconciliation.repository.ReconciliationSummaryRepository;
import com.nitin.payment.reconciliation.repository.SettlementFileRepository;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReconciliationService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);
    private final SettlementFileRepository fileRepository;
    private final ReconciliationRecordRepository recordRepository;
    private final ReconciliationSummaryRepository summaryRepository;
    private final ReconciliationEventPublisher eventPublisher;

    public ReconciliationSummary upload(MultipartFile file) {
        try {
            SettlementFile settlementFile = new SettlementFile();
            settlementFile.setOriginalFileName(file.getOriginalFilename());
            settlementFile.setFileType(Objects.requireNonNullElse(file.getContentType(), "unknown"));
            settlementFile.setStatus("PROCESSING");
            SettlementFile savedFile = this.fileRepository.save(settlementFile);
            List<BankRow> rows = this.parse(file);
            HashMap seen = new HashMap();
            List<ReconciliationRecord> records = rows.stream().map(row -> this.toRecord(savedFile.getId(), (BankRow)row, seen)).toList();
            this.recordRepository.saveAll(records);
            ReconciliationSummary summary = this.buildSummary(savedFile.getId(), records);
            savedFile.setStatus("COMPLETED");
            this.fileRepository.save(savedFile);
            this.eventPublisher.publishCompleted(savedFile.getId(), summary);
            log.info("Reconciliation completed fileId={}", (Object)savedFile.getId());
            return summary;
        }
        catch (Exception ex) {
            throw new FileProcessingException("Unable to process settlement file", (Throwable)ex);
        }
    }

    public ReconciliationSummary summary(Long fileId) {
        return this.summaryRepository.findByFileId(fileId).orElseThrow(() -> new ResourceNotFoundException("Summary not found for file: " + fileId));
    }

    public List<ReconciliationRecord> mismatches(Long fileId) {
        return this.recordRepository.findByFileIdAndReconciliationStatusNot(fileId, "MATCHED");
    }

    public String report(Long fileId) {
        StringBuilder csv = new StringBuilder("internalTransactionId,bankTransactionId,internalAmount,bankAmount,internalStatus,bankStatus,status,reason\n");
        this.recordRepository.findByFileId(fileId).forEach(r -> csv.append(String.join((CharSequence)",", this.safe(r.getInternalTransactionId()), this.safe(r.getBankTransactionId()), this.value(r.getInternalAmount()), this.value(r.getBankAmount()), this.safe(r.getInternalStatus()), this.safe(r.getBankStatus()), this.safe(r.getReconciliationStatus()), this.safe(r.getMismatchReason()))).append('\n'));
        return csv.toString();
    }

    private List<BankRow> parse(MultipartFile file) throws Exception {
        String name = Objects.requireNonNullElse(file.getOriginalFilename(), "").toLowerCase();
        if (name.endsWith(".xlsx")) {
            ArrayList<BankRow> rows = new ArrayList<BankRow>();
            Workbook workbook = WorkbookFactory.create((InputStream)file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                rows.add(new BankRow(this.cell(row, 0), this.cell(row, 1), new BigDecimal(this.cell(row, 2)), this.cell(row, 3)));
            }
            return rows;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));){
            List<BankRow> list = reader.lines().skip(1L).filter(line -> !line.isBlank()).map(line -> line.split(",")).map(cols -> new BankRow(cols[0], cols[1], new BigDecimal(cols[2]), cols[3])).toList();
            return list;
        }
    }

    private ReconciliationRecord toRecord(Long fileId, BankRow row, Map<String, Long> seen) {
        ReconciliationRecord record = new ReconciliationRecord();
        record.setFileId(fileId);
        record.setInternalTransactionId(row.internalTransactionId());
        record.setBankTransactionId(row.bankTransactionId());
        record.setInternalAmount(row.amount());
        record.setBankAmount(row.amount());
        record.setInternalStatus("SUCCESS");
        record.setBankStatus(row.status());
        seen.merge(row.bankTransactionId(), 1L, Long::sum);
        if (seen.get(row.bankTransactionId()) > 1L) {
            record.setReconciliationStatus("DUPLICATE");
            record.setMismatchReason("Duplicate bank transaction id");
        } else if (!"SUCCESS".equalsIgnoreCase(row.status())) {
            record.setReconciliationStatus("MISMATCHED");
            record.setMismatchReason("Bank status differs from expected success");
        } else {
            record.setReconciliationStatus("MATCHED");
        }
        return record;
    }

    private ReconciliationSummary buildSummary(Long fileId, List<ReconciliationRecord> records) {
        ReconciliationSummary summary = new ReconciliationSummary();
        summary.setFileId(fileId);
        summary.setTotalRecords(records.size());
        summary.setMatchedCount(records.stream().filter(r -> "MATCHED".equals(r.getReconciliationStatus())).count());
        summary.setMismatchedCount(records.stream().filter(r -> "MISMATCHED".equals(r.getReconciliationStatus())).count());
        summary.setDuplicateCount(records.stream().filter(r -> "DUPLICATE".equals(r.getReconciliationStatus())).count());
        summary.setMissingCount(records.stream().filter(r -> "MISSING".equals(r.getReconciliationStatus())).count());
        return (ReconciliationSummary)this.summaryRepository.save(summary);
    }

    private String cell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? "" : cell.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String value(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    @Generated
    public ReconciliationService(SettlementFileRepository fileRepository, ReconciliationRecordRepository recordRepository, ReconciliationSummaryRepository summaryRepository, ReconciliationEventPublisher eventPublisher) {
        this.fileRepository = fileRepository;
        this.recordRepository = recordRepository;
        this.summaryRepository = summaryRepository;
        this.eventPublisher = eventPublisher;
    }

    private record BankRow(String internalTransactionId, String bankTransactionId, BigDecimal amount, String status) {
    }
}
