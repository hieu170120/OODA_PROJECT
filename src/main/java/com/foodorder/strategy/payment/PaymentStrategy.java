package com.foodorder.strategy.payment;

public interface PaymentStrategy {

    /**
     * Mã phương thức thanh toán (VD: COD, BANKING, WALLET). Mỗi bean strategy một mã duy nhất.
     * Resolver chuẩn hóa uppercase — không cần enum tập trung.
     */
    String getMethodCode();

    boolean processPayment(double amount, String transactionId);

    /**
     * true: chỉ kiểm tra hợp lệ khi đặt hàng, không hoàn tất thanh toán ngay (demo: thu khi giao).
     */
    default boolean isDeferredPayment() {
        return false;
    }
}
