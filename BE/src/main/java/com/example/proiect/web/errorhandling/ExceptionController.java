package com.example.proiect.web.errorhandling;

import com.example.proiect.exceptions.EmployeeNotFoundException;
import com.example.proiect.model.Error;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();

        if (mostSpecificCause instanceof DateTimeParseException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Expected format: yyyy-MM-dd");
        }

        if(mostSpecificCause instanceof InvalidFormatException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid position format. Expected values: JUNIOR_DEV / MID_DEV / SENIOR_DEV");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter: " + parameterName);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A server error occurred");
    }


    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Error> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        log.error(ex.getMessage());

        Error error = Error.builder()
                .errorCode("NOT_FOUND")
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}
