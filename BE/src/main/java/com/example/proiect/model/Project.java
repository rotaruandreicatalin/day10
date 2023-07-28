package com.example.proiect.model;

import com.example.proiect.model.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "project name is mandatory")
    private String name;
    private String description;
    @Column(name = "monthly_cost")
    private double monthlyCost;

    @OneToMany(mappedBy = "project")
    private List<Employee> employees;

}