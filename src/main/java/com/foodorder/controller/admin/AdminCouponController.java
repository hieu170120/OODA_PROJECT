package com.foodorder.controller.admin;

import com.foodorder.dto.CouponRequestDTO;
import com.foodorder.dto.CouponResponseDTO;
import com.foodorder.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminCouponController {

    private final CouponService couponService;

    public AdminCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public String showCouponList(Model model, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        List<CouponResponseDTO> coupons = couponService.getAllCoupons().stream()
                .map(CouponResponseDTO::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("coupons", coupons);
        return "admin/coupon-list";
    }

    @PostMapping("/coupons/add")
    public String addCoupon(@ModelAttribute CouponRequestDTO couponDTO, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        couponService.addCoupon(couponDTO.toEntity());
        return "redirect:/admin/coupons";
    }

    @PostMapping("/coupons/update/{couponCode}")
    public String updateCoupon(@PathVariable String couponCode,
                               @ModelAttribute CouponRequestDTO couponDTO,
                               HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        couponService.updateCoupon(couponCode, couponDTO.toEntity());
        return "redirect:/admin/coupons";
    }

    @GetMapping("/coupons/delete/{couponCode}")
    public String deleteCoupon(@PathVariable String couponCode, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        couponService.deleteCoupon(couponCode);
        return "redirect:/admin/coupons";
    }
}