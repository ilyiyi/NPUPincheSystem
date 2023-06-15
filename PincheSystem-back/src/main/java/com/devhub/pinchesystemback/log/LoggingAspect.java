package com.devhub.pinchesystemback.log;


import com.devhub.pinchesystemback.domain.Log;
import com.devhub.pinchesystemback.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author wak
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LoggingAspect {

    @Resource
    private LogService service;

    @Resource
    private RedisTemplate<String, String> template;

    @Around("@annotation(com.devhub.pinchesystemback.advice.annotation.Log)")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request;
        String requestUrl = null;
        String requestType = null;
        String requestMethod = null;
        String requestArgs = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {

                request = attributes.getRequest();
                // 请求地址
                requestUrl = request.getRequestURL().toString();
                // 请求类型 get，post等
                requestType = request.getMethod();
                // 请求方法，具体方法名
                requestMethod = joinPoint.getSignature().getName();
                // 方法参数
                requestArgs = Arrays.toString(joinPoint.getArgs());
            }
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            float totalTime = (System.currentTimeMillis() - start) / 1000.0f;


            log.info("当前操作用户：{}", getUsername());
            log.info("请求地址：{}, 请求类型：{}, 请求方法：{}, 方法参数：{}, 执行时间：{}s", requestUrl, requestType, requestMethod, requestArgs, totalTime);
            // 数据库操作
            Log myLog = new Log();
            myLog.setUrl(requestUrl);
            myLog.setType(requestType);
            myLog.setMethod(requestMethod);
            myLog.setArgs(requestArgs);
            myLog.setTotalTime(totalTime);
            myLog.setOperateUser(getUsername());

            service.addLog(myLog);

            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前操作用户的用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return (String) template.opsForHash().get("cur", "username");
    }


}
