package com.foodorder.uml.repository;

import com.foodorder.uml.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
