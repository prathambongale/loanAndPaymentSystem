package com.bancx.lnpsystem.service.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreatePaymentRequestDto;
import com.bancx.lnpsystem.enums.StatusEnum;
import com.bancx.lnpsystem.model.Loan;
import com.bancx.lnpsystem.repository.LoanRepository;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Should record a payment and reduce the loan balance")
    void shouldRecordPaymentAndReduceBalance() {
        double paymentAmount = 100000.0;
        CreatePaymentRequestDto request = new CreatePaymentRequestDto(1L, paymentAmount);

        String result = paymentService.createPayment(request);

        assertEquals(ApplicationConstants.SUCCESS, result);

        Optional<Loan> updatedLoan = loanRepository.getLoanDetails(1L);
        assertTrue(updatedLoan.isPresent(), "Loan should still exist");

        Loan loan = updatedLoan.get();
        assertEquals(paymentAmount, loan.getPaymentCollected(),
                "Payment collected should equal the payment amount");
        assertEquals(2500000.0 - paymentAmount, loan.getBalanceAmount(),
                "Balance should be reduced by the payment amount");
        assertEquals(StatusEnum.ACTIVE.toString(), loan.getStatus(),
                "Loan should remain ACTIVE when balance is still outstanding");
    }

    @Test
    @DisplayName("Should return error message when payment exceeds remaining balance (overpayment)")
    void shouldReturnErrorOnOverpayment() {
        Optional<Loan> loanOpt = loanRepository.getLoanDetails(1L);
        assertTrue(loanOpt.isPresent());
        double currentBalance = loanOpt.get().getBalanceAmount();
        double overpaymentAmount = currentBalance + 1000.0;

        CreatePaymentRequestDto request = new CreatePaymentRequestDto(1L, overpaymentAmount);

        String result = paymentService.createPayment(request);

        assertTrue(result.contains("Amount paid is greater than balanced loan amount"),
                "Expected overpayment error message but got: " + result);
    }

    @Test
    @DisplayName("Should move loan to SETTLED status when paid in full")
    void shouldSettleLoanWhenPaidInFull() {
        loanRepository.createLaon(9999L, StatusEnum.ACTIVE.toString(), 10000.0, 60, 0.0, 10000.0);

        CreatePaymentRequestDto request = new CreatePaymentRequestDto(9999L, 10000.0);
        String result = paymentService.createPayment(request);

        assertEquals(ApplicationConstants.SUCCESS, result);

        Optional<Loan> settledLoan = loanRepository.getLoanDetails(9999L);
        assertTrue(settledLoan.isPresent(), "Loan should exist after full payment");

        Loan loan = settledLoan.get();
        assertEquals(StatusEnum.SETTLED.toString(), loan.getStatus(),
                "Loan status should be SETTLED after full payment");
        assertEquals(10000.0, loan.getPaymentCollected(),
                "Total payment collected should match the loan amount");
        assertEquals(0.0, loan.getBalanceAmount(),
                "Balance should be zero after full payment");
    }
}
