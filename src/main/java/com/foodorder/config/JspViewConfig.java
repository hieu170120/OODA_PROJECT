package com.foodorder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Đảm bảo JSP được resolve trước Thymeleaf (nếu cả hai cùng classpath).
 */
@Configuration
public class JspViewConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver jsp = new InternalResourceViewResolver();
        jsp.setPrefix("/WEB-INF/views/");
        jsp.setSuffix(".jsp");
        jsp.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registry.viewResolver(jsp);
    }
}
