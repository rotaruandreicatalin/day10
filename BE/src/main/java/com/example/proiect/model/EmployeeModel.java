package com.example.proiect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmployeeModel {


    @NotNull(message = "first name is mandatory")
    private String firstName;
    @NotNull(message = "last name is mandatory")

    private String lastName;
    @Valid
    private Position position;
    private double salary;
    @Email
    private String email;

    @NotNull(message = "hire date is mandatory")
    private LocalDate hireDate;
    @JsonIgnore
    @ManyToOne

    @Valid
    private Project project;
    private String projectName;
}