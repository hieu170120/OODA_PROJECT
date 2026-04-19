package com.foodorder.strategy.payment;

import com.foodorder.enums.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ánh xạ mã phương thức (Enum) → bean {@link PaymentStrategy}.
 * Mọi strategy được Spring đăng ký tự động; thêm loại mới chỉ cần class implement interface.
 */
@Component
public class PaymentStrategyResolver {

    private final Map<PaymentMethod, PaymentStrategy> strategiesByCode;

    public PaymentStrategyResolver(List<PaymentStrategy> strategies) {
        Map<PaymentMethod, PaymentStrategy> map = new HashMap<>();
        for (PaymentStrategy strategy : strategies) {
            PaymentMethod code = strategy.getMethodCode();
            if (code == null) {
                throw new IllegalStateException("PaymentStrategy " + strategy.getClass().getName()
                        + " phải trả về getMethodCode() khác null.");
            }
            if (map.put(code, strategy) != null) {
                throw new IllegalStateException("Trùng mã thanh toán: " + code);
            }
        }
        this.strategiesByCode = Map.copyOf(map);
    }

    /**
     * @param method mã phương thức; null được coi là không hợp lệ
     */
    public PaymentStrategy resolve(PaymentMethod method) {
        if (method == null) {
            return null;
        }
        return strategiesByCode.get(method);
    }
}