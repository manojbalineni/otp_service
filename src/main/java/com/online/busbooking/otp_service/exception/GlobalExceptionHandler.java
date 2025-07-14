package com.online.busbooking.otp_service.exception;

import com.online.busbooking.otp_service.DTO.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException exception){
        ErrorResponse errorResponse =  errorResponseHandler(exception.getCode(), exception.getMessage() , exception.getHttpStatus());
        return new ResponseEntity<>(errorResponse , exception.getHttpStatus());
    }

    private ErrorResponse errorResponseHandler(String code, String message, HttpStatus httpStatus) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(code);
        response.setErrorMessage(message);
        response.setHttpStatus(httpStatus.value());
        response.setTimeStamp(getTimeStamp());
        return response;
    }

    private String getTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }
}
