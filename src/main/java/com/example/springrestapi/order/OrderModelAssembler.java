package com.example.springrestapi.order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.springrestapi.status.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {

        EntityModel<Order> orderModel = new EntityModel<>(
            order,
            linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders")
        );

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(
                linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderModel;
    }
}