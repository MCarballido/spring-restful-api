package com.example.springrestapi.phone;

import java.util.List;

public interface PhoneService {
    List<Phone> getPhonesByEmployee(long employeeId);
    Phone getPhone(long id);
    Phone createPhone(long employeeId, Phone phone);
    Phone updatePhone(Phone phone, long id);
    void deletePhone(long id);
}
