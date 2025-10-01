package com.example.springjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

@SpringBootApplication
public class SpringJdbcExApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(SpringJdbcExApplication.class, args);
        String[] beanDefinationName = context.getBeanDefinitionNames();
        Arrays.stream(beanDefinationName)
                .sorted()
                .forEach(System.out::println);
    }
}
