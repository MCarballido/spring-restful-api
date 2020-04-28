package com.example.springrestapi.phone;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository repository;

    public PhoneServiceImpl(PhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Phone> getPhonesByEmployee(long id) {
        List<Phone> list = repository.findByEmployeeId(id);
        return list;
    }
}
