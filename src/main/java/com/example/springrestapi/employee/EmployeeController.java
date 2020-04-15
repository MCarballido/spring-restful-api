package com.example.springrestapi.employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = repository.findAll().stream()
            .map(employee -> new EntityModel<>(
                employee,
                linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees")
            ))
            .collect(Collectors.toList());

        return new CollectionModel<>(
            employees,
            linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel()
        );
    }

    @PostMapping("/employees")
    Employee createEmployee(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getEmployee(@PathVariable long id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

//        return new EntityModel<>(
//            employee,
//            linkTo(methodOn(EmployeeController.class).getEmployee(id)).withSelfRel(),
//            linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees")
//        );
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    Employee updateEmployee(@RequestBody Employee employee, @PathVariable long id) {
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

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable long id) {
        repository.deleteById(id);
    }
}
