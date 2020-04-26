package com.example.springrestapi.customer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CustomerAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        return new EntityModel<>(
            customer,
            linkTo(methodOn(CustomerController.class).getCustomer(customer.getId())).withSelfRel(),
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers")
        );
    }
}
