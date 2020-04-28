package com.example.springrestapi.phone;

import java.util.List;

public interface PhoneService {
    List<Phone> getPhonesByEmployee(long id);
}
