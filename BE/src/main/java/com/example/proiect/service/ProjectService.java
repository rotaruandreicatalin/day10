package com.example.proiect.service;

import com.example.proiect.model.Employee;
import com.example.proiect.model.Project;
import com.example.proiect.repository.EmployeeRepository;
import com.example.proiect.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }
    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public String deleteProjectById(Integer id) {
        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getProject() != null && employee.getProject().getId().equals(id))
                .collect(Collectors.toList());
        String nume = projectRepository.findById(id).orElse(null).getName();
        for(Employee employee : employees){
            employee.setProject(null);
            employee.setProjectName(null);
        }
        projectRepository.deleteById(id);
        return "Proiectul " + nume + " a fost sters cu succes.";
    }

    public String updateProject(Integer id, Project updatedProject){
        Project project = projectRepository.findById(id).orElse(null);
        if(project != null) {
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            project.setMonthlyCost(updatedProject.getMonthlyCost());
            project.setEmployees(null);
            projectRepository.save(project);
        }
        return "Proiectul a fost modificat cu succes!";
    }

    public double monthlyCost(Integer id){
        Project project = projectRepository.findById(id).orElse(null);
        double monthlyCost = 0;
        if(project != null){
            monthlyCost = project.getMonthlyCost();
            for(Employee employee : project.getEmployees()){
                monthlyCost += employee.getSalary();
            }
        }
        return monthlyCost;
    }

}
