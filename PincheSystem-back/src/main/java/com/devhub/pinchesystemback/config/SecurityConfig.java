package com.devhub.pinchesystemback.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 这是SpringSecurity的配置类，一旦看到extends WebSecurityConfigurerAdapter就应该想到这是安全相关的配置
 * 这里继承的是WebSecurityConfigurerAdapter，
 */
@Configuration
@Import(BCryptPasswordEncoder.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;


    /**
     * 下面这个方法用于配置认证方式(如何根据用户名获取用户信息,以及密码加密方式)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/assets/**", "/vendor/**");
    }

    /**
     * 这里用来配置我们自定义的登录页面
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")   //自定义登录页面
                .loginProcessingUrl("/user/login") // 登录访问路径
                .failureUrl("/error") // 处理异常的controller
//                .authenticationDetailsSource(authenticationDetailsSource)  //自定义的资源要配置进去
                .defaultSuccessUrl("/index").permitAll()  // 登录成功后默认页面
                .and().authorizeRequests()
                .antMatchers("/", "/hello", "/user/register", "/user/login", "/admin/login", "/assets/**", "/vendor/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()//设置哪些页面和请求不需要登录就能访问
                .anyRequest().authenticated()
                .and().csrf().disable();  //关闭csrf防护
    }
}