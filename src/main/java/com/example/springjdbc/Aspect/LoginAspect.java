package com.example.springjdbc.Aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {

    @Pointcut("execution(* com.example.springjdbc.Service.EmployeeService.*(..))")
    public void lob(){};
    @Around("lob()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("Aspect log is called");
        Object res =  joinPoint.proceed();
        System.out.println("Aspect after log called");
        return res;
    }
}
