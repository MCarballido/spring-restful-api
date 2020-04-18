package com.example.springrestapi;

import com.example.springrestapi.employee.Employee;
import com.example.springrestapi.employee.EmployeeRepository;

import com.example.springrestapi.order.Order;
import com.example.springrestapi.order.OrderRepository;
import com.example.springrestapi.status.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading: " + employeeRepository.save(new Employee("John Doe", "burglar")));
            log.info("Preloading: " + employeeRepository.save(new Employee("Jane Doe", "thief")));
            log.info("Preloading: " + orderRepository.save(new Order("iPhone X", Status.COMPLETED)));
            log.info("Preloading: " + orderRepository.save(new Order("Samsung Galaxy", Status.IN_PROGRESS)));
        };
    }
}
