package com.example.springrestapi.phone;

import com.example.springrestapi.employee.EmployeeRepository;
import com.example.springrestapi.employee.Employee;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository repository;
    private final EmployeeRepository employeeRepository;

    public PhoneServiceImpl(PhoneRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository; 
    }

    @Override
    public List<Phone> getAllPhones(long id) {
        List<Phone> phones = repository.findByEmployeeId(id);
        return phones;
    }

    @Override
    public Phone getPhone(long employeeId, long id) {
        Phone phone = repository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find a phone for the provided ID.")
        );

        if (phone.getEmployee().getId() != employeeId) {
            throw new IllegalArgumentException("Could not find an employee for the provided ID.");
        }

        return phone;
    }

    @Override
    public Phone createPhone(long employeeId, Phone phone) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> 
            new EntityNotFoundException("Could not find an employee for the provided ID.")    
        );

        phone.setEmployee(employee);

        return repository.save(phone);
    }

    @Override
    public Phone updatePhone(long employeeId, long id, Phone phone) {
        return repository
            .findById(id)
            .map(entity -> {
                entity.setNumber(phone.getNumber());
                return repository.save(entity);
            })
            .orElseGet(() -> {
                phone.setId(id);
                return repository.save(phone);
            });
    } // missing verify employeeId

    @Override
    public void deletePhone(long employeeId, long id) {
        repository.deleteById(id);
    }

}
