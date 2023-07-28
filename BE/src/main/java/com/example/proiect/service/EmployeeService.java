package com.example.proiect.service;

import com.example.proiect.exceptions.EmployeeNotFoundException;
import com.example.proiect.model.Employee;
import com.example.proiect.model.Project;
import com.example.proiect.repository.EmployeeRepository;
import com.example.proiect.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final ProjectRepository projectRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee addEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    public String deleteEmployeeById(Integer id) {
        if(employeeRepository.findById(id).orElse(null) == null ) return "Angajatul nu exista!";
        String nume = employeeRepository.findById(id).orElse(null).getFirstName() + " " + employeeRepository.findById(id).orElse(null).getLastName();
        employeeRepository.deleteById(id);

        // trace < debug < info < warn < error
        log.debug("Angajatul " + nume + " a fost sters cu succes.");
        log.info("1.am primit rq");
        log.debug("1.1 Am facut o validare optionala");
        log.info("2.urmeaza sa inserez in baza");
        log.info("3.am inserat cu succes in baza");


        return "Angajatul " + nume + " a fost sters cu succes.";
    }

    /**
     * 202 - Accepted (cand e cu succes)
     * 500 - Internal error (cand sunt pb de server, de ex nu merge conexiunea cu BD)
     * 404 - Daca nu exista resursa employeeId || projectId
     *
     * Daca avem erori de client (4XX) sau de server (5xx) nu returnam obiectul de succes (EmployeeModel)
     * ci un obiect de eroare, care e generic pe toata aplicatia
     *
     * De exe:
     * Error{
     *     String errorCode;
     *     String errorMessage;
     * }
     * @param employeeId
     * @param projectId
     * @return
     */
    public boolean allocateProjectToEmployee(Integer employeeId, Integer projectId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        /**
         * exemplu aruncare exceptie custom
         */
//        if(employee==null){
//            throw new EmployeeNotFoundException("Employee not found", HttpStatus.NOT_FOUND);
//        }
//
        if(employee != null && project != null){
            employee.setProject(project);
            employee.setProjectName(project.getName());
            employeeRepository.save(employee);
            return true;
            //return "Angajatul " + employee.getFirstName() + " " + employee.getLastName() + " a fost alocat cu succes proiectului " + project.getName() + "!";
        }
       // else throw ProjectNotFound(...);
        return false;
        //return "Angajatul nu a putut fi alocat cu succes proiectului! (angajatul sau proiectul nu exista)";
    }

    public List<Employee> getEmployeesByProjectId(Integer projectId) {
        return employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getProject() != null && employee.getProject().getId().equals(projectId))
                .collect(Collectors.toList());
    }

    public String raiseSalary(LocalDate date, Integer number) {
        List<Employee> employees = getEmployeesWithHireDateBefore(date);
        checkIfEmployeesExist(employees);
        //if (employees.size() < 1) return; "Nu a fost actualizat salariul niciunui angajat!";

        raiseSalaryForEligibleEmployees(number, employees);

        return "Au fost actualizati " + employees.size() + " angajati.";
    }

    private void raiseSalaryForEligibleEmployees(Integer number, List<Employee> employees) {
        for (Employee employee : employees) {
            employee.setSalary(employee.getSalary() + (employee.getSalary() * number / 100));
//            employee.setSalary(employee.getSalary() * ( 1 + (number/100)));
            employeeRepository.save(employee);
        }
    }

    public void checkIfEmployeesExist( List<Employee> employees){
        if (employees.size() < 1) {
            //logica de business
            //nu stiu ce logi
            //throw exception
        }

    }
    private List<Employee> getEmployeesWithHireDateBefore(LocalDate date) {
        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getHireDate().isBefore(date))
                .collect(Collectors.toList());
        return employees;
    }
}