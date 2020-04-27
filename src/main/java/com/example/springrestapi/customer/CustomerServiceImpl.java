package com.example.springrestapi.customer;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Customer getCustomer(long id) {
        return repository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find a Customer for the provided ID."
        ));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer, long id) {
        return repository
            .findById(id)
            .map(entity -> {
                entity.setName(customer.getName());
                return repository.save(entity);
            })
            .orElseGet(() -> {
                customer.setId(id);
                return repository.save(customer);
            });
    }

    @Override
    public void deleteCustomer(long id) {
        repository.deleteById(id);
    }
}
