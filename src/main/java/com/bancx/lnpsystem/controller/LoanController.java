package com.bancx.lnpsystem.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreateLoanRequestDto;
import com.bancx.lnpsystem.dto.GetLoanDetailsDto;
import com.bancx.lnpsystem.dto.ResponseDto;
import com.bancx.lnpsystem.service.loan.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/loan", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {

    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    @Operation(tags = "Loan Management", summary = "Service to get loan details by ID", description = "This endpoint retrieves the details of a Loan based on the provided loan ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved loan details"),
            @ApiResponse(responseCode = "400", description = "Invalid loan ID supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto> createLoan(@Validated @RequestBody CreateLoanRequestDto createLoanRequestDto) {

        String response = loanService.creatLoan(createLoanRequestDto);

        if (Objects.isNull(response)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.failed());
        }

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/loans/{id}")
    @Operation(tags = "Loan Management", summary = "Service to get loan details by ID", description = "This endpoint retrieves the details of a Loan based on the provided loan ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved loan details"),
            @ApiResponse(responseCode = "400", description = "Invalid loan ID supplied"),
            @ApiResponse(responseCode = "404", description = "Loan not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto> getLoanDetails(@Validated @PathVariable("id") Long loanId) {
        var response = loanService.getLoanDetails(loanId);

        return switch (response) {
            case GetLoanDetailsDto r -> ResponseEntity.ok(ResponseDto.success(r));
            case ApplicationConstants.NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.nonFound("No records found for provided loan ID"));
            case null -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.failed());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.failed());
        };

    }

}
