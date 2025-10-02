package com.example.springjdbc.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employees")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String role;

    @Min(0)
    private Double salary;

    private LocalDate hireDate = LocalDate.now();

    // One-to-One: Employee -> Address
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    // One-to-Many: Employee -> SalarySlip
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @BatchSize(size = 10)
    private List<SalarySlip> salarySlips = new ArrayList<>();

    // Many-to-Many: Employee <-> Project
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "employee_project",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @BatchSize(size = 10)
    private Set<Project> projects = new HashSet<>();

    public Employee() {}

    public Employee(String name, String role, Double salary) {
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    // convenience helpers
    public void addSalarySlip(SalarySlip slip) {
        slip.setEmployee(this);
        this.salarySlips.add(slip);
    }

    public void removeSalarySlip(Long slipId) {
        salarySlips.removeIf(slip -> {
            boolean match = Objects.equals(slip.getId(), slipId);
            if (match) slip.setEmployee(null);
            return match;
        });
    }

    public void addProject(Project p) {
        this.projects.add(p);
        p.getEmployees().add(this);
    }

    public void removeProject(Project p) {
        this.projects.remove(p);
        p.getEmployees().remove(this);
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public List<SalarySlip> getSalarySlips() { return salarySlips; }
    public void setSalarySlips(List<SalarySlip> salarySlips) { 
        this.salarySlips.clear();
        if (salarySlips != null) {
            salarySlips.forEach(this::addSalarySlip);
        }
    }

    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", address=" + address +
                ", salarySlips=" + salarySlips +
                ", projects=" + projects +
                '}';
    }
}
