package com.bancx.lnpsystem.dto;

public record ResponseDto<T> (String code, String message, T data) {
    
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>("0000", "Success", data);
    }

    public static <T> ResponseDto<T> failed() {
        return new ResponseDto<>("0001", "Failed", null);
    }

    public static <T> ResponseDto<T> nonFound(T data) {
        return new ResponseDto<>("0002", "Record Not Found", data);
    }

    public static <T> ResponseDto<T> badRequest(T data) {
        return new ResponseDto<>("0003", "Validation Exception", data);
    }
}
