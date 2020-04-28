package com.example.springrestapi.phone;

import com.example.springrestapi.employee.Employee;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@Table(name = "Phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull(message = "The number cannot be null.")
    @NotBlank(message = "The number cannot be empty.")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Employee employee;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;

        Phone phone = (Phone) o;
        return Objects.equals(phone.number, this.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.number);
    }
}
