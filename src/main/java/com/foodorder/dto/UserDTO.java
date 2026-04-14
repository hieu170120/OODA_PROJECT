package com.foodorder.dto;

import com.foodorder.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String role;

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole("USER");
        return dto;
    }
}
