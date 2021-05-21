package com.zh.ppholic_server_demo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerRestExceptionHandler {

    // add an exception handler for GP
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(Exception exc){
        
        // create CustomerErrorResponse
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), 
                                                            exc.getMessage(),
                                                            System.currentTimeMillis());

        // return ResponseEntity
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}