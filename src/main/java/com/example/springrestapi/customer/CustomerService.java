package com.example.springrestapi.customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomer(long id);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer, long id);
    void deleteCustomer(long id);
}
