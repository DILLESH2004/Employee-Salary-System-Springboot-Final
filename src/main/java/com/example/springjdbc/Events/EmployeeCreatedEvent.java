package com.example.springjdbc.Events;



public class EmployeeCreatedEvent {
    public final Long EmpId;


    public EmployeeCreatedEvent(Long empId) {
        EmpId = empId;
    }

    public Long getEmpId(){
        return EmpId;
    }

}
