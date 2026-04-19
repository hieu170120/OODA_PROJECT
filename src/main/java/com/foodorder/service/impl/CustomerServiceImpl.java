package com.foodorder.service.impl;

import com.foodorder.entity.Customer;
import com.foodorder.repository.CustomerRepository;
import com.foodorder.service.CustomerService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer;
        }
        return null;
    }

    @Override
    public Customer register(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setRewardPoints(0);
        return customerRepository.save(customer);
    }

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
