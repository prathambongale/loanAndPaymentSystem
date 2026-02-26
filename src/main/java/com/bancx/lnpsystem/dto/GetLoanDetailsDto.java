package com.bancx.lnpsystem.dto;

public record GetLoanDetailsDto(Long loanId, String status, double loanAmount, double remainingBalance,
        int termInMonths, double totalPaymentCollected) {

}
