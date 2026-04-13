package com.foodorder.service.impl;

import com.foodorder.entity.User;
import com.foodorder.repository.UserRepository;
import com.foodorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        // Note: For a real project, use password hashing (e.g., BCrypt).
        // Using plain text here for simplicity based on current setup.
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
