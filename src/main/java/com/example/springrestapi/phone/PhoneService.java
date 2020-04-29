package com.example.springrestapi.phone;

import java.util.List;

public interface PhoneService {
    List<Phone> getAllPhones(long employeeId);
    Phone getPhone(long employeeId, long id);
    Phone createPhone(long employeeId, Phone phone);
    Phone updatePhone(long employeeId, long id, Phone phone);
    void deletePhone(long employeeId, long id);
}
