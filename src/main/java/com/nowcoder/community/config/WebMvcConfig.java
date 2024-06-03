package com.nowcoder.community.config;

import com.nowcoder.community.controller.LoginController;
import com.nowcoder.community.controller.intercepter.LoginTicketintercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketintercepter loginTicketintercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(loginTicketintercepter)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }



}
