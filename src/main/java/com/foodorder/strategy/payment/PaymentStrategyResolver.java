package com.foodorder.strategy.payment;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Ánh xạ mã phương thức (chuỗi) → bean {@link PaymentStrategy}.
 * Mọi strategy được Spring đăng ký tự động; thêm loại mới chỉ cần class implement interface.
 */
@Component
public class PaymentStrategyResolver {

    private final Map<String, PaymentStrategy> strategiesByCode;

    public PaymentStrategyResolver(List<PaymentStrategy> strategies) {
        Map<String, PaymentStrategy> map = new HashMap<>();
        for (PaymentStrategy strategy : strategies) {
            String code = normalize(strategy.getMethodCode());
            if (code == null || code.isEmpty()) {
                throw new IllegalStateException("PaymentStrategy " + strategy.getClass().getName()
                        + " phải trả về getMethodCode() khác rỗng.");
            }
            if (map.put(code, strategy) != null) {
                throw new IllegalStateException("Trùng mã thanh toán: " + code);
            }
        }
        this.strategiesByCode = Map.copyOf(map);
    }

    /**
     * @param methodCode mã từ client (không phân biệt hoa thường); null hoặc rỗng được coi là không hợp lệ — gọi từ service sau khi gán mặc định.
     */
    public PaymentStrategy resolve(String methodCode) {
        return strategiesByCode.get(normalize(methodCode));
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return null;
        }
        String t = raw.trim();
        return t.isEmpty() ? null : t.toUpperCase(Locale.ROOT);
    }
}