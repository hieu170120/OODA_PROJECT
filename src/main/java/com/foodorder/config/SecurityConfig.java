package com.foodorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Cho phép tất cả mọi người truy cập vào các trang này mà không cần đăng nhập
                .requestMatchers("/", "/home", "/login", "/order", "/order/add-item", "/order/checkout", "/api/v1/orders/**").permitAll()
                .anyRequest().permitAll() // Hoặc tạm thời cho phép tất cả để test dễ dàng
            )
            .csrf(csrf -> csrf.disable())
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
