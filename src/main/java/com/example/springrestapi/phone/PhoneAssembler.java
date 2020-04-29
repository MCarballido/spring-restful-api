package com.example.springrestapi.phone;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PhoneAssembler implements RepresentationModelAssembler<Phone, EntityModel<Phone>> {

    @Override
    public EntityModel<Phone> toModel(Phone phone) {
        return new EntityModel<>(
            phone,
            linkTo(methodOn(PhoneController.class).getPhone(phone.getId(), phone.getEmployee().getId())).withSelfRel(),
            linkTo(methodOn(PhoneController.class).getPhonesByEmployee(phone.getEmployee().getId())).withRel("phones")
        );
    }
}
