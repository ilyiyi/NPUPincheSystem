package com.devhub.pinchesystemback.advice;

import com.devhub.pinchesystemback.constant.ResultCodeEnum;
import com.devhub.pinchesystemback.exception.BusinessException;
import com.devhub.pinchesystemback.exception.IllegalOperationException;
import com.devhub.pinchesystemback.exception.NotFoundException;
import com.devhub.pinchesystemback.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 * 需要注意的是，这个处理器[只能处理经过Filter之后才发生的异常]，它可以处理Interceptor抛出的异常、Controller抛出的异常、Service抛出的异常、Mapper抛出的异常。
 * 对于在Filter中抛出的异常，例如认证失败导致抛出异常，不在这里处理，而是有专门的处理器（详情请见SecurityConfig类）
 * 请求的顺序：Filter(过滤器)->Interceptor(拦截器)->Controller->Service->Mapper
 * 有人也管Mapper叫做DAO层
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 参数校验失败的异常处理器
     * 对于request中body部分的校验，校验失败抛出的异常是MethodArgumentNotValidException，但是我们捕获的不是MethodArgumentNotValidException
     * 因为BindException是MethodArgumentNotValidException的父类，
     * 所以这里捕获BindException不仅可以顺带捕获它的子类MethodArgumentNotValidException，还可以捕获request中query参数的校验失败，相当于一个方法处理了多种不同类型的参数校验失败
     */
    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, HttpServletRequest request, Model model) {
        String message = formatBindException(e);
        handleError(model, message);
        log.warn(formatException(e, request, message, false));
        return "error";
    }


    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotAllowed(Exception e, HttpServletRequest request, Model model) {
        log.warn(formatException(e, request, null, false));
        handleError(model, "请求方式不支持");
        return "error";
    }


    /**
     * 请求格式不对
     */
    @ExceptionHandler({ServletRequestBindingException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public String handleBadRequest(Exception e, HttpServletRequest request, Model model) {
        handleError(model, "请求格式不对");
        log.warn(formatException(e, request, null, false));
        return "error";
    }


    /**
     * 请求的接口权限不足，例如一个普通用户去请求管理员才能用的接口（这是授权的时候抛出的异常，在进入Controller之前就被拦住，更不会进入Service层）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(Exception e, HttpServletRequest request, Model model) {
        handleError(model, "权限不足");
        log.warn(formatException(e, request, null, false));
        return "error";
    }

    /**
     * 请求的操作非法，例如一个普通用户要删除其他普通用户发布的内容（已通过授权并进入了Controller层，但是在Service层发现操作非法而抛出的异常）
     */
    @ExceptionHandler(IllegalOperationException.class)
    public String handleIllegalOperationException(Exception e, HttpServletRequest request, Model model) {
        handleError(model, "操作非法");
        log.warn(formatException(e, request, null, false));
        return "error";
    }

    /**
     * 请求资源不存在
     */
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception e, HttpServletRequest request, Model model) {
        handleError(model, "请求资源不存在");
        log.warn(formatException(e, request, null, true));
        return "error";
    }

    /**
     * 业务异常，可细分为多种情况，可见ResultCodeEnum
     */
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e, HttpServletRequest request, Model model) {

        log.warn(formatException(e, request, null, true));
        handleError(model, e.getMessage());
        return "error";
    }


    /**
     * 如果前面的处理器都没拦截住，最后兜底
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request, Model model) {
        handleError(model, "服务器内部错误");
        return "error";
    }

    public void handleError(Model model, String errorMsg) {
        model.addAttribute("errorMessage", errorMsg);
        model.addAttribute("indexPage", "/index");
    }


    /**
     * 把异常信息格式化成自己喜欢的格式，这个方法用于格式化Exception
     */
    public static String formatException(Exception e, HttpServletRequest request, String message, boolean stackRequired) {
        StringBuilder sb = new StringBuilder();
        sb.append("[出现异常]")
                .append("\n<请求>  ").append(request.getMethod()).append("  ").append(request.getRequestURI())
                .append("\n<类型>  ").append(e.getClass())
                .append("\n<信息>  ").append(message != null ? message : e.getMessage());
        if (stackRequired) {
            sb.append("\n<堆栈>\n");
            for (StackTraceElement traceElement : e.getStackTrace()) {
                sb.append("\tat ").append(traceElement).append("\n");
            }
        }
        return sb.toString();
    }


    /**
     * 把异常信息格式化成自己喜欢的格式，这个方法用于格式化BindException
     */
    public static String formatBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : fieldErrors) {
            //提示：error.getField()得到的是校验失败的字段名字，error.getDefaultMessage()得到的是校验失败的原因
            sb.append(error.getField())
                    .append("=[")
                    .append(error.getDefaultMessage())
                    .append("]  ");
        }
        return sb.toString();

    }
}