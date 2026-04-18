package com.foodorder.controller.admin;

import com.foodorder.dto.CouponRequestDTO;
import com.foodorder.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {

    private final CouponService couponService;

    public AdminCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public String showCoupons(Model model, HttpSession session) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("coupons", couponService.getAllCoupons());
        model.addAttribute("newCoupon", new CouponRequestDTO());
        return "admin/coupon-list";
    }

    @PostMapping("/add")
    public String addCoupon(@ModelAttribute("newCoupon") CouponRequestDTO couponRequestDTO,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        try {
            couponService.createCoupon(couponRequestDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm coupon thành công.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/coupons";
    }

    @PostMapping("/update/{id}")
    public String updateCoupon(@PathVariable("id") String couponId,
                               @ModelAttribute CouponRequestDTO couponRequestDTO,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        try {
            couponService.updateCoupon(couponId, couponRequestDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật coupon thành công.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/coupons";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable("id") String couponId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (session.getAttribute("LOGGED_IN_ADMIN") == null) {
            return "redirect:/admin/login";
        }

        try {
            couponService.deleteCoupon(couponId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa coupon thành công.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/coupons";
    }
}
