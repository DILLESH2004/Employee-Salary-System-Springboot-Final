package com.example.springjdbc.Controller;

import com.example.springjdbc.Entity.ErrorResponse;
import com.example.springjdbc.Exceptions.EmployeeNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeNotFound.class)
    public ResponseEntity<?> HandleEmployeeNotFoundException(EmployeeNotFound e){
        ErrorResponse error =  new ErrorResponse(LocalDateTime.now(), "Employee Not Found", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
