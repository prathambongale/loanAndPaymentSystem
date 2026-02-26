package com.bancx.lnpsystem.service.loan;

import com.bancx.lnpsystem.dto.CreateLoanRequestDto;

public sealed interface LoanService permits LoanServiceImpl {
    
    String creatLoan(CreateLoanRequestDto createLoanRequest);

    <T> T getLoanDetails(Long loanId);
}
