package com.example.springrestapi.phone;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PhoneController {

    private final PhoneAssembler assembler;
    private final PhoneService service;

    public PhoneController(PhoneAssembler assembler, PhoneService service) {
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/employees/{employeeId}/phones")
    public List<Phone> getPhonesByEmployee(@PathVariable long employeeId) {
        List<Phone> phones = service.getPhonesByEmployee(employeeId);
        return phones;
    }
}
