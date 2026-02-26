package com.bancx.lnpsystem.service.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreateLoanRequestDto;
import com.bancx.lnpsystem.enums.StatusEnum;
import com.bancx.lnpsystem.model.Loan;
import com.bancx.lnpsystem.repository.LoanRepository;

@SpringBootTest
class LoanServiceImplTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Should create a loan and store it in the database")
    void shouldCreateLoanAndStoreInDatabase() {

        double loanAmount = 50000.0;
        int termInMonths = 120;
        CreateLoanRequestDto request = new CreateLoanRequestDto(loanAmount, termInMonths);

        String result = loanService.creatLoan(request);

        assertEquals(ApplicationConstants.SUCCESS, result);

        Optional<Loan> savedLoan = loanRepository.getLoanDetails(2L);
        assertNotNull(savedLoan);
        assertEquals(true, savedLoan.isPresent(), "Loan should be present in the database");
        assertEquals(StatusEnum.ACTIVE.toString(), savedLoan.get().getStatus());
        assertEquals(loanAmount, savedLoan.get().getLoanAmount());
        assertEquals(termInMonths, savedLoan.get().getTermInMonths());
        assertEquals(0.0, savedLoan.get().getPaymentCollected());
        assertEquals(0.0, savedLoan.get().getBalanceAmount());
    }
}
