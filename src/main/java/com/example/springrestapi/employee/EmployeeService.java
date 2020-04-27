package com.example.springrestapi.employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();
    Employee getEmployee(long id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Employee employee, long id);
    void deleteEmployee(long id);
}
