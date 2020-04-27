package com.example.springrestapi.employee;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployee(long id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Could not find an Employee for the provided ID.")
            );
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, long id) {
        return repository.findById(id)
            .map(emp -> {
                emp.setName(employee.getName());
                emp.setRole(employee.getRole());
                return repository.save(emp);
            })
            .orElseGet(() -> {
                employee.setId(id);
                return repository.save(employee);
            });
    }

    @Override
    public void deleteEmployee(long id) {
        repository.deleteById(id);
    }
}
