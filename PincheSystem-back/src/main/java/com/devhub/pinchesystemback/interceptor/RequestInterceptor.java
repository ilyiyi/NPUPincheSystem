package com.devhub.pinchesystemback.interceptor;

import com.devhub.pinchesystemback.domain.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置response响应数据类型为json和编码为utf-8
        response.setContentType("application/json;charset=utf-8");
        // 判断对象是否是映射到一个方法，如果不是则直接通过
        if (!(handler instanceof HandlerMethod)) {
            // instanceof运算符是用来在运行时指出对象是否是特定类的一个实例
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;


        Method method = handlerMethod.getMethod();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String uri = request.getRequestURI();
        if (loginUser == null) {
            response.sendRedirect("/public/login");
            return false;
        }
        if (uri.startsWith("/admin") && loginUser.getRole().equals((byte) 0)) {
            response.sendRedirect("/public/index");
            return false;
        }
        return true;
    }
}
