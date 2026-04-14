package com.foodorder.service;

import com.foodorder.entity.User;

public interface UserService {
    User authenticate(String email, String password);
    User register(User user);
}
