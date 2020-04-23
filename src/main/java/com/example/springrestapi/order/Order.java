package com.example.springrestapi.order;

import com.example.springrestapi.status.Status;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private Status status;

    public Order() {}

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }
}