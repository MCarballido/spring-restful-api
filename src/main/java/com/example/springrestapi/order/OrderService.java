package com.example.springrestapi.order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    Order getOrder(long id);
    Order createOrder(Order order);
    Order cancel(long id);
    Order complete(long id);
}
