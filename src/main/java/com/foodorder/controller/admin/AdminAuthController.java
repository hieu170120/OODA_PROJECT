package com.foodorder.controller.admin;

import com.foodorder.dto.UserDTO;
import com.foodorder.entity.Manager;
import com.foodorder.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller quản lý xác thực admin (Manager) của hệ thống
 * Xử lý login/logout cho phần quản trị viên
 */
@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final ManagerService managerService;

    public AdminAuthController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String processAdminLogin(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    HttpSession session) {
        // Xác thực Manager từ bảng managers
        Manager manager = managerService.authenticate(email, password);
        if (manager != null) {
            // Tạo session cho manager
            UserDTO managerDTO = new UserDTO();
            managerDTO.setUserId(manager.getUserId());
            managerDTO.setEmail(manager.getEmail());
            managerDTO.setFullName(manager.getFullName());
            managerDTO.setRole("MANAGER");
            
            session.setAttribute("LOGGED_IN_ADMIN", managerDTO);
            return "redirect:/admin/dishes";
        } else {
            return "redirect:/admin/login?error";
        }
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout";
    }
}
