package com.example.proiect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    @NotNull(message = "first name is mandatory")
    private String firstName;
    @NotNull(message = "last name is mandatory")
    @Column(name = "last_name")
    private String lastName;
    @Valid
    private Position position;
    private double salary;
    @Email
    private String email;
    @Column(name = "hire_date")
    @NotNull(message = "hire date is mandatory")
    private LocalDate hireDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    @Valid
    private Project project;
    @Column(name = "project_name")
    private String projectName;



}