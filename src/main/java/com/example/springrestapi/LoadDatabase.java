package com.example.springrestapi;

import com.example.springrestapi.employee.Employee;
import com.example.springrestapi.employee.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading: " + repository.save(new Employee("John Doe", "burglar")));
            log.info("Preloading: " + repository.save(new Employee("Jane Doe", "thief")));
        };
    }
}
