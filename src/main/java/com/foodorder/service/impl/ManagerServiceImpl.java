package com.foodorder.service.impl;

import com.foodorder.entity.Manager;
import com.foodorder.repository.ManagerRepository;
import com.foodorder.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public Manager authenticate(String email, String password) {
        Manager manager = managerRepository.findByEmail(email).orElse(null);
        // Note: Trong dự án thực tế, nên dùng password hashing (e.g., BCrypt)
        // Đây chỉ dùng plain text để đơn giản theo setup hiện tại
        if (manager != null && manager.getPassword().equals(password)) {
            return manager;
        }
        return null;
    }
}
