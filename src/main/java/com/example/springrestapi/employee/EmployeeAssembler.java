package com.example.springrestapi.employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.springrestapi.phone.PhoneController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    /***
     * It converts a non-recourse object into a resource-based object (EntityModel<obj>)
     * @param entity
     * @return entity model of the e
     */
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return new EntityModel<>(
            employee,
            linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"),
            linkTo(methodOn(PhoneController.class).getAllPhones(employee.getId())).withRel("phones")
        );
    }
}
