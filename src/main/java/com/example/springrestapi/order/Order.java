package com.example.springrestapi.order;

import com.example.springrestapi.customer.Customer;
import com.example.springrestapi.employee.Employee;
import com.example.springrestapi.status.Status;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "The description cannot be null.")
    @NotBlank(message = "The description cannot be empty.")
    private String description;

    @NotNull(message = "The status cannot be null.")
    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    @NotNull(message = "The employee cannot be null.")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "employee_id_fk"))
    private Employee employee;

    @NotNull(message = "The customer cannot be null.")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "customer_id_fk"))
    private Customer customer;

    public Order() {};

    public Order(String description) {
        this.description = description;
    }
}