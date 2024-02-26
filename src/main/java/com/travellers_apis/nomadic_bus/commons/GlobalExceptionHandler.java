package com.travellers_apis.nomadic_bus.commons;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.travellers_apis.nomadic_bus.models.CustomErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDetails> exceptionHandler(Exception ex, WebRequest web) {
        CustomErrorDetails error = new CustomErrorDetails(LocalDateTime.now(), "Validation error!",
                web.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<CustomErrorDetails> adminExceptionHandler(AdminException ex, WebRequest web) {
        CustomErrorDetails error = new CustomErrorDetails(LocalDateTime.now(), "Validation error!",
                web.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDetails> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        CustomErrorDetails error = new CustomErrorDetails(LocalDateTime.now(), "Validation error!", null);
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(er -> errors.append(er.getDefaultMessage() + "\n"));
        error.setDetails(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorDetails busExceptionHandler(BusException ex, WebRequest wb) {
        CustomErrorDetails error = CustomErrorDetails.builder().time(LocalDateTime.now()).message("Wrong bus details")
                .details(wb.getDescription(false)).build();
        return error;
    }
}
