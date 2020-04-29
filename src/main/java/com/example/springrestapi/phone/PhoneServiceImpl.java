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
    public List<Phone> getPhonesByEmployee(long id) {
        return repository.findByEmployeeId(id);
    }

    @Override
    public Phone getPhone(long id) {
        return repository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find a phone for the provided ID.")
        );
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
    public Phone updatePhone(long id, Phone phone) {
        Phone entity = repository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Could not find a phone for the provided ID.")
        );

        entity.setNumber(phone.getNumber());

        return repository.save(entity);
    }

    @Override
    public void deletePhone(long id) {
        repository.deleteById(id);
    }

}
