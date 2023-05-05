package com.devhub.pinchesystemback.filter;

import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这是一个Filter（过滤器），其职能为：
 * （1）如果请求没带token，则[不设置]Authentication标识为true，并且放行
 * （2）如果请求带了token，但是token是无效的，抛出异常，不会进入下一个Filter，异常会被认证失败处理器捕获，返回HTTP401
 * （3）如果请求带了token，而且token是有效的，[设置]Authentication标识为true，并且放行
 * 仅仅用@Component注入IOC容器还不够，还需要在SpringConfig里面配置
 * SpringSecurity是由一串过滤器链实现的，而这里就是我们自定义的一个过滤器
 */
@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("=================请求进来了=================");
        log.info("[新请求]\n<方法>  {}\n<URI>  {}\n<Query>  {}\n<主机>  {}",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), request.getRemoteHost());
        String authorization = request.getHeader("authorization");

        if (StringUtils.hasText(authorization)) {
            User user;
            try {
                user = jwtUtil.getUserFromToken(authorization);
                MDC.put("currentUser", user);
                MDC.get("currentUser");
            } catch (Exception e) {
                log.warn("在解析token时出错，错误原因:{}", e.getMessage());
                throw new BadCredentialsException(e.getMessage());
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );

        }

        filterChain.doFilter(request, response);
        log.info("=================请求离开了=================");

    }
}