package com.example.springrestapi.employee;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(long id) {
        super("Could not find employee " + id + "!");
    }
}
