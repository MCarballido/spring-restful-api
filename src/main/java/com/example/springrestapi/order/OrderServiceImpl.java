package com.example.springrestapi.order;

import com.example.springrestapi.customer.Customer;
import com.example.springrestapi.customer.CustomerRepository;
import com.example.springrestapi.employee.Employee;
import com.example.springrestapi.employee.EmployeeRepository;
import com.example.springrestapi.status.Status;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, EmployeeRepository employeeRepository,
                            CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(long id) {
        return orderRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find an Order for the provided ID.")
        );
    }

    @Override
    public Order createOrder(Order order) {
        Employee employee = employeeRepository
            .findById(order.getEmployee().getId())
            .orElseThrow(() -> new EntityNotFoundException("Could not find an Employee for the provided ID."));
        Customer customer = customerRepository
            .findById(order.getCustomer().getId())
            .orElseThrow(() -> new EntityNotFoundException("Could not find a Customer for the provided ID."));

        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setStatus(Status.IN_PROGRESS);

        return orderRepository.save(order);
    }

    @Override
    public Order cancel(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find an Order for the provided ID.")
        );

        if (order.getStatus() != Status.IN_PROGRESS) {
            throw new ResponseStatusException(
                HttpStatus.METHOD_NOT_ALLOWED,
                "It is not possible to cancel an order that is in the " + order.getStatus() + " status."
            );
        }

        order.setStatus(Status.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public Order complete(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find an Order for the provided ID.")
        );

        if (order.getStatus() != Status.IN_PROGRESS) {
            throw new ResponseStatusException(
                HttpStatus.METHOD_NOT_ALLOWED,
                "It is not possible to complete an order that is in the " + order.getStatus() + " status."
            );
        }

        order.setStatus(Status.COMPLETED);
        return orderRepository.save(order);
    }
}
