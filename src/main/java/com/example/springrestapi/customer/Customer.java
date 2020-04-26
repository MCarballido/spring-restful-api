package com.example.springrestapi.customer;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull(message = "The name cannot be null.")
    @NotBlank(message = "The name cannot be empty.")
    private String name;

    public Customer() {}

    public Customer(String name) {
        this.name = name;
    }
}
