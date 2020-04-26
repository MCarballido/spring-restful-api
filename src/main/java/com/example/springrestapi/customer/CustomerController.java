package com.example.springrestapi.customer;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerRepository repository;
    private final CustomerAssembler assembler;

    public CustomerController(CustomerRepository repository, CustomerAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/customers")
    CollectionModel<EntityModel<Customer>> getAllCustomers() {
        List<EntityModel<Customer>> customers = repository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            customers,
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel()
        );
    }

    @GetMapping("/customers/{id}")
    EntityModel<Customer> getCustomer(@PathVariable long id) {
        Customer customer = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find a Customer for the provided ID."));

        return assembler.toModel(customer);
    }

    @PostMapping("/customers")
    ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {

        Customer entity = repository.save(customer);
        EntityModel<Customer> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/customers/{id}")
    ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, @PathVariable long id) {
        Customer updatedCustomer = repository
            .findById(id)
            .map(entity -> {
                entity.setName(customer.getName());
                return repository.save(entity);
            })
            .orElseGet(() -> {
                customer.setId(id);
                return repository.save(customer);
            });

        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);

        return ResponseEntity
            .ok()
            .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        repository.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
