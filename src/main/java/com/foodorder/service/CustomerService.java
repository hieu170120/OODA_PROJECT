package com.foodorder.service;

import com.foodorder.entity.Customer;

public interface CustomerService {
    Customer authenticate(String email, String password);
    Customer register(Customer customer);
}
