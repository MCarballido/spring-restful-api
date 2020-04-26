package com.example.springrestapi.order;

import com.example.springrestapi.customer.Customer;
import com.example.springrestapi.customer.CustomerRepository;
import com.example.springrestapi.employee.Employee;
import com.example.springrestapi.employee.EmployeeRepository;
import com.example.springrestapi.status.Status;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
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
public class OrderController {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository repository;
    private final OrderAssembler assembler;

    public OrderController(CustomerRepository customerRepository, EmployeeRepository employeeRepository, OrderRepository repository, OrderAssembler assembler) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            orders,
            linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()
        );
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> getOrder(@PathVariable long id) {
        Order order = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Order for the provided ID."));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> createOrder(@Valid @RequestBody Order order) {
        Employee employee = employeeRepository
            .findById(order.getEmployee().getId())
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Employee for the provided ID."));
        Customer customer = customerRepository
            .findById(order.getCustomer().getId())
            .orElseThrow(() -> new EntityNotFoundException("Could not find a Customer for the provided ID."));

        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setStatus(Status.IN_PROGRESS);

        Order newOrder = repository.save(order);

        return ResponseEntity
            .created(linkTo(methodOn(OrderController.class).getOrder(newOrder.getId())).toUri())
            .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable long id) {
        Order order = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Order for the provided id."));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new VndErrors.VndError(
                "Method not allowed",
                "You can't cancel an order that is in the " + order.getStatus() + " status."
            ));
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<?> complete(@PathVariable long id) {
        Order order = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Order for the provided id"));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new VndErrors.VndError(
                "Method not allowed",
                "You can't cancel an order that is in the " + order.getStatus() + " status"
            ));
    }

}
