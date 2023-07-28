package com.example.proiect.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends RuntimeException{

    @Getter
    @Setter
    private HttpStatus status;
    public EmployeeNotFoundException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

}
