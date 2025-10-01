package com.example.springjdbc.Repository;

import com.example.springjdbc.Entity.SalarySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SalarySlipRepository extends JpaRepository<SalarySlip, Long> {}
