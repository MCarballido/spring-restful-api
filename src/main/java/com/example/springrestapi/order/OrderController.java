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

    private final OrderRepository repository;
    private final OrderAssembler assembler;
    private final OrderService service;

    public OrderController(OrderRepository repository, OrderAssembler assembler, OrderService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orders = service.getAllOrders().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            orders,
            linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()
        );
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> getOrder(@PathVariable long id) {
        Order order = service.getOrder(id);

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> createOrder(@Valid @RequestBody Order order) {
        Order newOrder = service.createOrder(order);

        return ResponseEntity
            .created(linkTo(methodOn(OrderController.class).getOrder(newOrder.getId())).toUri())
            .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable long id) {
        Order entity = service.cancel(id);

        return ResponseEntity.ok(assembler.toModel(entity));
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<?> complete(@PathVariable long id) {
        Order entity = service.complete(id);

        return ResponseEntity.ok(assembler.toModel(entity));

//        return ResponseEntity
//            .status(HttpStatus.METHOD_NOT_ALLOWED)
//            .body(new VndErrors.VndError(
//                "Method not allowed",
//                "You can't cancel an order that is in the " + order.getStatus() + " status"
//            ));
    }

}
