package com.devhub.pinchesystemback.config;


import com.devhub.pinchesystemback.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 这是SpringSecurity的配置类，一旦看到extends WebSecurityConfigurerAdapter就应该想到这是安全相关的配置
 * 这里继承的是WebSecurityConfigurerAdapter，
 */
@Configuration
@Import(BCryptPasswordEncoder.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 下面这个方法用于配置认证方式(如何根据用户名获取用户信息,以及密码加密方式)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 这里用来配置我们自定义的登录页面
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/user/login")   //自定义登录页面
                .loginProcessingUrl("/user/login") // 登录访问路径
                .failureUrl("/error") // 处理异常的controller
//                .authenticationDetailsSource(authenticationDetailsSource)  //自定义的资源要配置进去
                .defaultSuccessUrl("/hello").permitAll()  // 登录成功后默认页面
                .and().authorizeRequests()
                .antMatchers("/", "/hello", "/user/register", "/user/login", "/admin/login", "/assets/**","/vendor/**").permitAll()  //设置哪些页面和请求不需要登录就能访问
                .anyRequest().authenticated()
                .and().csrf().disable();  //关闭csrf防护
    }


    /**
     * 这个类就是我们自定义如何去从数据库，根据用户名拿到用户信息的
     */
    @Service
    public static class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserMapper userMapper;

        /**
         * 这步就是根据输入Controller层输入地username去数据库拿出加密之后的password，准确地说，它是把整个用户都拿出来了，封装成UserDetails
         * 至于Controller层输入的密码是否和数据库里保存地密码一样，这个判断是由SpringSecurity帮我们做的
         * 它会把Controller层输入的密码按照PasswordEncoder进行加密，然后去和数据库里的加密后的密码判断是否匹配
         * 如果不匹配就会抛异常。（抛异常也没事，我已经在UserServiceImpl的login方法里面用try-catch捕获了）
         */
        @Override
        public UserDetails loadUserByUsername(String username) {
            UserDetails user = userMapper.selectByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名为" + username + "的账户不存在");
            }
            return user;
        }
    }


}