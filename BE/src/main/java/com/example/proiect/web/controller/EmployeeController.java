package com.example.proiect.web.controller;

import com.example.proiect.model.Employee;
import com.example.proiect.model.EmployeeModel;
import com.example.proiect.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;


    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody @Valid Employee employee){
        Employee response = service.addEmployee(employee);
        if(response == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id){
        Employee response = service.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        /**
         * scenariile de succes diferite (200, 204, 206 whatever) pot fi tratate la nivel de controller
         * return in functie de fiecare scenariu
         */
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
        return ResponseEntity.ok(service.deleteEmployeeById(id));
    }

    @PutMapping("/{employeeId}/allocate-project/{projectId}")
    public ResponseEntity<String> allocateProjectToEmployee(@PathVariable Integer employeeId,
                                                            @PathVariable Integer projectId) {
        boolean output = service.allocateProjectToEmployee(employeeId, projectId);
        if (output) {
            // În caz de succes, returnăm codul 200 (OK) și un mesaj
            return new ResponseEntity<>("Angajatul a fost alocat cu succes proiectului!", HttpStatus.OK);
        } else {
            // În caz de eroare, returnăm codul 404 (Not Found) și un alt mesaj
            return new ResponseEntity<>("Angajatul sau proiectul transmis nu exista!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<Employee>> getEmployeesByProjectId(@PathVariable Integer projectId) {
        return ResponseEntity.ok(service.getEmployeesByProjectId(projectId));
    }

    @PutMapping("/raise-salary")
    public ResponseEntity<String> raiseSalary(@Valid @NotNull(message = "hire date is mandatory") @RequestParam LocalDate date,
                                              @Valid @NotNull(message = "raise procentage is mandatory") @RequestParam Integer number){
        return ResponseEntity.ok(service.raiseSalary(date, number));
    }
}
