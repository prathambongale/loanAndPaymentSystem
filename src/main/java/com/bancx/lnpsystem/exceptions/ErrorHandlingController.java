package com.bancx.lnpsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bancx.lnpsystem.dto.ResponseDto;

@ControllerAdvice
public class ErrorHandlingController {
    
    @ExceptionHandler(CustomeException.class)
    public ResponseEntity<ResponseDto> handRunTimeExceptions(CustomeException customeException) {        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.badRequest(customeException.getMessage()));
    }
}
