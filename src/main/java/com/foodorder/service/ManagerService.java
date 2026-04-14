package com.foodorder.service;

import com.foodorder.entity.Manager;

public interface ManagerService {
    Manager authenticate(String email, String password);
}
