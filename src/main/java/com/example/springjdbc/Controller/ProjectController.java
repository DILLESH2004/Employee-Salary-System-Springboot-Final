package com.example.springjdbc.Controller;

import com.example.springjdbc.Entity.Project;
import com.example.springjdbc.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private EmployeeService service;

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        Project saved = service.createProject(project);
        return ResponseEntity.created(URI.create("/api/projects/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Project> all() {
        return service.allProjects();
    }
}
