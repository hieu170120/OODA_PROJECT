package com.foodorder.controller;

import com.foodorder.dto.CustomerDTO;
import com.foodorder.entity.Customer;
import com.foodorder.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        Customer customer = customerService.authenticate(email, password);
        if (customer != null) {
            // Chuyển Entity Customer thành DTO, tránh lưu Entity (chứa Password, db properties) lên session
            CustomerDTO customerDTO = CustomerDTO.fromEntity(customer);
            session.setAttribute("LOGGED_IN_USER", customerDTO);
            return "redirect:/menu";
        } else {
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("email") String email,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        try {
            // Validate passwords match
            if (!password.equals(confirmPassword)) {
                model.addAttribute("registerError", "Mật khẩu không khớp. Vui lòng kiểm tra lại.");
                model.addAttribute("firstName", firstName);
                model.addAttribute("lastName", lastName);
                model.addAttribute("email", email);
                model.addAttribute("phone", phone);
                return "register";
            }

            // Validate password length
            if (password.length() < 6) {
                model.addAttribute("registerError", "Mật khẩu phải có ít nhất 6 ký tự.");
                model.addAttribute("firstName", firstName);
                model.addAttribute("lastName", lastName);
                model.addAttribute("email", email);
                model.addAttribute("phone", phone);
                return "register";
            }

            // Create new customer
            Customer newCustomer = new Customer();
            newCustomer.setFullName(firstName + " " + lastName);
            newCustomer.setEmail(email);
            newCustomer.setPhone(phone);
            newCustomer.setPassword(password);

            // Register customer
            customerService.register(newCustomer);

            return "redirect:/login?success=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registerError", e.getMessage());
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "register";
        } catch (Exception e) {
            model.addAttribute("registerError", "Có lỗi xảy ra. Vui lòng thử lại.");
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "register";
        }
    }
}
