package com.example.springrestapi.employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
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
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            employees,
            linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel()
        );
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getEmployee(@PathVariable long id) {
        Employee employee = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Employee for the provided id"));

        return assembler.toModel(employee);
    }

    @PostMapping("/employees")
    ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        Employee entity = repository.save(employee);
        EntityModel<Employee> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable long id) {
        Employee updatedEmployee = repository.findById(id)
            .map(emp -> {
                emp.setName(employee.getName());
                emp.setRole(employee.getRole());
                return repository.save(emp);
            })
            .orElseGet(() -> {
                employee.setId(id);
                return repository.save(employee);
            });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity
            .accepted()
            .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable long id) {
        repository.deleteById(id);

        return  ResponseEntity.noContent().build();
    }
}
