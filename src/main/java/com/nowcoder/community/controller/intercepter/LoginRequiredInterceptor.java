package com.nowcoder.community.controller.intercepter;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) { //如果拦截到的是方法，把拦截到的method的对象拿出来

            System.out.println(request);
            System.out.println(request.getHeaderNames());
            System.out.println(request.toString());
            System.out.println(request.getMethod());
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null){
                response.sendRedirect(request.getContextPath() + "/login");
                return false;

            }

        }
        return true;

    }
}
