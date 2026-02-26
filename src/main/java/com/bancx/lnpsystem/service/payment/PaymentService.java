package com.bancx.lnpsystem.service.payment;

import com.bancx.lnpsystem.dto.CreatePaymentRequestDto;


public sealed interface PaymentService permits PaymentServiceImpl {
    
    String createPayment(CreatePaymentRequestDto createPaymentRequestDto);
}
