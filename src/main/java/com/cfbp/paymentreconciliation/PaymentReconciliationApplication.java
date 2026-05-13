package com.cfbp.paymentreconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PaymentReconciliationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentReconciliationApplication.class, args);
    }
}
