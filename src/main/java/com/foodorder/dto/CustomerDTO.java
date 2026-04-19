package com.foodorder.dto;

import com.foodorder.entity.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String userId;
    private String fullName;
    private String email;
    private String role;

    public static CustomerDTO fromEntity(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId(customer.getUserId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setRole("CUSTOMER");
        return dto;
    }
}
