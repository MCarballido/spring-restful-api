package com.example.springrestapi.customer;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerAssembler assembler;
    private final CustomerService service;

    public CustomerController(CustomerAssembler assembler, CustomerService service) {
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/customers")
    CollectionModel<EntityModel<Customer>> getAllCustomers() {

        List<Customer> customers = service.getAllCustomers();

        List<EntityModel<Customer>> customerEntityModels = customers.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            customerEntityModels,
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel()
        );
    }

    @GetMapping("/customers/{id}")
    EntityModel<Customer> getCustomer(@PathVariable long id) {
        Customer customer = service.getCustomer(id);

        return assembler.toModel(customer);
    }

    @PostMapping("/customers")
    ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {

        Customer entity = service.createCustomer(customer);
        EntityModel<Customer> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/customers/{id}")
    ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, @PathVariable long id) {
        Customer entity = service.updateCustomer(customer, id);

        EntityModel<Customer> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .ok()
            .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        service.deleteCustomer(id);

        return ResponseEntity.ok().build();
    }

}
