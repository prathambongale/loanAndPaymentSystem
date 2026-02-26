package com.bancx.lnpsystem.dto;

import com.bancx.lnpsystem.exceptions.CustomeException;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreatePaymentRequestDto(long loandId, double paymentAmt) {
    
    public CreatePaymentRequestDto {
        if (loandId < 1 ) {
            throw new CustomeException("Loan ID can't be less than 1");
        }

        if (paymentAmt < 1) {
            throw new CustomeException("Payment amount can't be less than R1");
        }
    }
}
