package com.bancx.lnpsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreatePaymentRequestDto;
import com.bancx.lnpsystem.dto.ResponseDto;
import com.bancx.lnpsystem.service.payment.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    @Operation(tags = "Payment Management", summary = "Service to create payment", description = "This endpoint creates an payment against the Loan ID provided in the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved loan details"),
            @ApiResponse(responseCode = "400", description = "Invalid loan ID supplied or Payment exceeds the remaining balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto> createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        var response = paymentService.createPayment(createPaymentRequestDto);

        return switch (response) {
            case ApplicationConstants.SUCCESS -> ResponseEntity.ok(ResponseDto.success("Payment Create Successfully"));
            case ApplicationConstants.NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.nonFound("No records found for provided loan ID"));
            case String s -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.badRequest(s));
            case null -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.failed());
        };
    }
}
