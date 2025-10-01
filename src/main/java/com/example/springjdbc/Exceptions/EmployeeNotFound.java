package com.example.springjdbc.Exceptions;

public class EmployeeNotFound extends  RuntimeException {
    public EmployeeNotFound(String message){
        super(message);
    }
}
