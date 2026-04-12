package com.foodorder.strategy.payment;

public interface PaymentStrategy {

    /**
     * Mã phương thức thanh toán (VD: COD, BANKING, WALLET). Mỗi bean strategy một mã duy nhất.
     * Resolver chuẩn hóa uppercase — không cần enum tập trung.
     */
    String getMethodCode();

    boolean processPayment(double amount, String transactionId);
<<<<<<< Updated upstream:src/main/java/com/foodorder/strategy/payment/PaymentStrategy.java
}
=======

    /**
     * true: chỉ kiểm tra hợp lệ khi đặt hàng, không hoàn tất thanh toán ngay (demo: thu khi giao).
     */
    default boolean isDeferredPayment() {
        return false;
    }
}
>>>>>>> Stashed changes:src/main/java/com/foodorder/strategy/PaymentStrategy.java
