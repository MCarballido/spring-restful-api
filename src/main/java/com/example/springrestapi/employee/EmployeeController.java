package com.example.springrestapi.employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeAssembler assembler;
    private final EmployeeService service;

    public EmployeeController(EmployeeAssembler assembler, EmployeeService service) {
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<Employee> emps = service.getAllEmployees();

        List<EntityModel<Employee>> employees = emps
            .stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            employees,
            linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel()
        );
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getEmployee(@PathVariable long id) {
        Employee employee = service.getEmployee(id);

        return assembler.toModel(employee);
    }

    @PostMapping("/employees")
    ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {

        Employee entity = service.createEmployee(employee);
        EntityModel<Employee> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable long id) {
        Employee updatedEmployee = service.updateEmployee(employee, id);

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity
            .ok()
            .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable long id) {
        service.deleteEmployee(id);

        return ResponseEntity.ok().build();
    }
}
