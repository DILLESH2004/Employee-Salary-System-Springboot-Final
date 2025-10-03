package com.example.springjdbc.Repository;

import com.example.springjdbc.Entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>    {
    List<Employee> findByRoleIgnoreCase(String role);
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.address LEFT JOIN FETCH e.salarySlips LEFT JOIN FETCH e.projects")
    Page<Employee> findAll(Pageable pageable);
}
