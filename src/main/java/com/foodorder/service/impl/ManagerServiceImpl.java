package com.foodorder.service.impl;

import com.foodorder.entity.Manager;
import com.foodorder.repository.ManagerRepository;
import com.foodorder.service.ManagerService;

import org.springframework.stereotype.Service;

@Service

public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public Manager authenticate(String email, String password) {
        Manager manager = managerRepository.findByEmail(email).orElse(null);
        if (manager != null && manager.getPassword().equals(password)) {
            return manager;
        }
        return null;
    }

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }
}
