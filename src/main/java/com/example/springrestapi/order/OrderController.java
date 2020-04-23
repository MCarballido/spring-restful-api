package com.example.springrestapi.order;

import com.example.springrestapi.status.Status;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    public OrderController(OrderRepository repository, OrderModelAssembler assembler) {
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
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Order for the provided id"));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> createOrder(@RequestBody Order order) {
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = repository.save(order);

        return ResponseEntity
            .created(linkTo(methodOn(OrderController.class).getOrder(newOrder.getId())).toUri())
            .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
//  ResponseEntity<RepresentationModel> cancel(@PathVariable long id) {
    ResponseEntity<?> cancel(@PathVariable long id) {
        Order order = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Order for the provided id"));

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
