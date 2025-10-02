package com.example.springjdbc.Service;

import com.example.springjdbc.Entity.*;
import com.example.springjdbc.Events.EmployeeCreatedEvent;
import com.example.springjdbc.Exceptions.EmployeeNotFound;
import com.example.springjdbc.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SalarySlipRepository salarySlipRepository;

    @Autowired
    private ProjectRepository projectRepository;


    private final ApplicationEventPublisher applicationEventPublisher;

    public EmployeeService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Employee create(Employee e) {
        // cascade will handle address and slips
        if (e.getProjects() != null && !e.getProjects().isEmpty()) {
            // ensure projects are managed entities
            Set<Project> managed = new HashSet<>();
            for (Project p : e.getProjects()) {
                if (p.getId() != null) {
                    managed.add(projectRepository.findById(p.getId()).orElseThrow(() -> new RuntimeException("Project not found: " + p.getId())));
                } else {
                    managed.add(projectRepository.save(p));
                }
            }
            e.setProjects(managed);
        }
        return employeeRepository.save(e);

    }

    @Cacheable("employee")
    public Employee get(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new    EmployeeNotFound("Employee not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Employee> all(int page,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        return employeeRepository.findAll(pageable);
    }

    public Employee update(Long id, Employee payload) {
        Employee existing = get(id);
        existing.setName(payload.getName());
        existing.setRole(payload.getRole());
        existing.setSalary(payload.getSalary());
        existing.setHireDate(payload.getHireDate());

        // address
        if (payload.getAddress() != null) {
            if (existing.getAddress() == null) {
                existing.setAddress(payload.getAddress());
            } else {
                Address a = existing.getAddress();
                Address np = payload.getAddress();
                a.setStreet(np.getStreet());
                a.setCity(np.getCity());
                a.setState(np.getState());
                a.setZipcode(np.getZipcode());
            }
        }

        // salary slips (replace)
        if (payload.getSalarySlips() != null && !payload.getSalarySlips().isEmpty()) {
            existing.getSalarySlips().clear();
            payload.getSalarySlips().forEach(existing::addSalarySlip);
        }

        // projects (replace)
        if (payload.getProjects() != null) {
            Set<Project> managed = new HashSet<>();
            for (Project p : payload.getProjects()) {
                if (p.getId() != null) {
                    managed.add(projectRepository.findById(p.getId()).orElseThrow(() -> new RuntimeException("Project not found: " + p.getId())));
                } else {
                    managed.add(projectRepository.save(p));
                }
            }
            existing.setProjects(managed);
        }

        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        Employee e = get(id);
        employeeRepository.delete(e);
    }

    // Attach/Detach helpers
    public Employee setAddress(Long empId, Address address) {
        Employee e = get(empId);
        e.setAddress(address);
        return employeeRepository.save(e);
    }

    public Employee addSalarySlip(Long empId, SalarySlip slip) {
        Employee e = get(empId);
        e.addSalarySlip(slip);
        return employeeRepository.save(e);
    }

    public Employee assignProject(Long empId, Long projectId) {
        Employee e = get(empId);
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        e.addProject(p);
        return employeeRepository.save(e);
    }

    public Employee unassignProject(Long empId, Long projectId) {
        Employee e = get(empId);
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        e.removeProject(p);
        return employeeRepository.save(e);
    }

    public Project createProject(Project p) {
        return projectRepository.save(p);
    }

    public List<Project> allProjects() {
        return projectRepository.findAll();
    }

    public SalarySlip getSlip(Long slipId) {
        return salarySlipRepository.findById(slipId).orElseThrow(() -> new RuntimeException("Salary slip not found: " + slipId));
    }
}
