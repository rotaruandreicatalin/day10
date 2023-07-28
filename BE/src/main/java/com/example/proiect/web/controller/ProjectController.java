package com.example.proiect.web.controller;

import com.example.proiect.model.Project;
import com.example.proiect.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private final ProjectService service;
//POST /projects - vezi in prima faza dca este suficient verbul + numele resursei (substantiv)
    //cand nu e suficient, poti sa adaugi verbe in numele resursei
    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody @Valid Project project){
        Project response = service.addProject(project);
        if(response == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(service.getProjectById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(){
        return ResponseEntity.ok(service.getAllProjects());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(service.deleteProjectById(id));
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<String> updateProject(@PathVariable(value = "id") Integer id,
                                                @RequestBody @Valid Project project){
        return ResponseEntity.ok(service.updateProject(id, project));
    }

    @GetMapping("/monthly-cost/{id}")
    public double monthlyCost(@PathVariable(value = "id") Integer id){
        return service.monthlyCost(id);
    }
}