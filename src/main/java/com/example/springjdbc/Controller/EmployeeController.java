package com.example.springjdbc.Controller;

import com.example.springjdbc.Entity.*;
import com.example.springjdbc.Service.CacheInspectionService;
import com.example.springjdbc.Service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private CacheInspectionService cacheInspectionService;

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = service.create(employee);
        return ResponseEntity.created(URI.create("/api/employees/" + saved.getId())).body(saved);
    }

    @GetMapping("/token")
    public CsrfToken getcsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public Page<Employee> all(@RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "size", defaultValue = "10") int size) {
        return service.all(page,size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") Long id, @RequestBody Employee payload) {
        return ResponseEntity.ok(service.update(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Attach address
    @PostMapping("/{id}/address")
    public ResponseEntity<Employee> setAddress(@PathVariable("id") Long id, @RequestBody Address address) {
        return ResponseEntity.ok(service.setAddress(id, address));
    }

    // Add salary slip
    @PostMapping("/{id}/salary-slips")
    public ResponseEntity<Employee> addSalarySlip(@PathVariable("id") Long id, @RequestBody SalarySlip slip) {
        return ResponseEntity.ok(service.addSalarySlip(id, slip));
    }

    // Assign/Unassign project
    @PostMapping("/{id}/projects/{projectId}")
    public ResponseEntity<Employee> assignProject(@PathVariable("id") Long id, @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(service.assignProject(id, projectId));
    }

    @DeleteMapping("/{id}/projects/{projectId}")
    public ResponseEntity<Employee> unassignProject(@PathVariable("id") Long id, @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(service.unassignProject(id, projectId));
    }

    @GetMapping("/Cache")
    public void getCacheData(){
        cacheInspectionService.printCacheContents("employee");
    }
}
