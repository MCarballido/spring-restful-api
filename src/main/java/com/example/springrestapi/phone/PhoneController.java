package com.example.springrestapi.phone;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public CollectionModel<EntityModel<Phone>> getPhonesByEmployee(@PathVariable long employeeId) {
        List<Phone> phones = service.getPhonesByEmployee(employeeId);

        List<EntityModel<Phone>> phonesEntities = phones
            .stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            phonesEntities,
            linkTo(methodOn(PhoneController.class).getPhonesByEmployee(employeeId)).withSelfRel()
        );
    }

    @GetMapping("/employees/{employeeId}/phones/{id}")
    public EntityModel<Phone> getPhone(@PathVariable long id, @PathVariable long employeeId) {
        Phone phone = service.getPhone(id);

        return assembler.toModel(phone);
    }

    @PostMapping("/employees/{employeeId}/phones")
    public ResponseEntity<?> createPhone(@Valid @RequestBody Phone phone, @PathVariable long employeeId) {
        Phone createdPhone = service.createPhone(employeeId, phone);
        EntityModel<Phone> phoneEntityModel = assembler.toModel(createdPhone);

        return ResponseEntity
            .created(phoneEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(phoneEntityModel);
    }

    @PutMapping("/employees/{employeeId}/phones/{id}")
    ResponseEntity<?> updatePhone(@Valid @RequestBody Phone phone, @PathVariable long id, @PathVariable long employeeId) {
        Phone entity = service.updatePhone(id, phone);

        EntityModel<Phone> entityModel = assembler.toModel(entity);

        return ResponseEntity
            .ok()
            .location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/employees/{employeeId}/phones/{id}")
    ResponseEntity<?> deletePhone(@PathVariable long id, @PathVariable long employeeId) {
        service.deletePhone(id);

        return ResponseEntity.noContent().build();
    }
}
