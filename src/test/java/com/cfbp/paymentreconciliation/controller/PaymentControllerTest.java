package com.cfbp.paymentreconciliation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cfbp.paymentreconciliation.dto.PaymentRequest;
import com.cfbp.paymentreconciliation.dto.PaymentResponse;
import com.cfbp.paymentreconciliation.enums.PaymentStatus;
import com.cfbp.paymentreconciliation.security.CustomUserDetailsService;
import com.cfbp.paymentreconciliation.security.JwtService;
import com.cfbp.paymentreconciliation.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createPaymentReturnsCreatedPayment() throws Exception {
        PaymentResponse response = response();
        when(paymentService.createPayment(any(PaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentReference").value("PAY-001"));
    }

    @Test
    void getPaymentsReturnsList() throws Exception {
        when(paymentService.getPayments()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentReference").value("PAY-001"));
    }

    private PaymentRequest request() {
        return new PaymentRequest("PAY-001", "Aarav", "ACC-1", "ACC-2",
                new BigDecimal("500.00"), "INR", "UPI");
    }

    private PaymentResponse response() {
        return new PaymentResponse(1L, "PAY-001", "Aarav", "ACC-1", "ACC-2",
                new BigDecimal("500.00"), "INR", "UPI", PaymentStatus.CREATED, null, false, null, null);
    }
}
