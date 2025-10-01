package com.example.springjdbc.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;

    private LocalDate deadline;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

    public Project() {}

    public Project(String projectName, LocalDate deadline) {
        this.projectName = projectName;
        this.deadline = deadline;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) { this.employees = employees; }
}
