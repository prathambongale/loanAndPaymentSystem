package com.bancx.lnpsystem.dto;

import com.bancx.lnpsystem.exceptions.CustomeException;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateLoanRequestDto(double loanAmount, int termInMonths) {
    
    public CreateLoanRequestDto {
        if (loanAmount < 5000 || loanAmount > 5000000) {
            throw new CustomeException("Loan amount can't be less than 5000 or grater than 5000000");
        }

        if (termInMonths < 60 || termInMonths > 360) {
            throw new CustomeException("Loan term can't be less than 60 months or grater than 360 months");
        }
    }
}
