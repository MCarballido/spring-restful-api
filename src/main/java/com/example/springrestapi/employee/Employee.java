package com.example.springrestapi.employee;

import com.example.springrestapi.phone.Phone;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull(message = "The name cannot be null.")
    @NotBlank(message = "The name cannot be empty.")
    private String name;

    @NotNull(message = "The role cannot be null.")
    @NotBlank(message = "The role cannot be empty.")
    private String role;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Phone> phones = new ArrayList<>();

    protected Employee() {}

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public void addPhone(Phone phone) {
        phone.setEmployee(this);
        phones.add(phone);
    }

    public void removePhone(Phone phone) {
        phone.setEmployee(null);
        phones.remove(phone);
    }
}
