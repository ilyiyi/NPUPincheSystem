package com.devhub.pinchesystemback.constant;

/**
 * 不同业务异常及其对应的状态码
 */
public enum ResultCodeEnum {

    /**
     * 参数校验失败
     */
    PARAM_VALIDATE_FAILED(10001, "参数校验失败"),
    /**
     * 用户名已存在（注册接口用）
     */
    USERNAME_ALREADY_EXIST(10002, "用户名已存在"),
    /**
     * 用户名或密码错误（登录接口用）
     */

    WRONG_USERNAME_OR_PASSWORD(10003, "用户名或密码错误"),
    /**
     * 重复操作
     */
    REPEAT_OPERATION(10004, "重复操作"),

    /**
     * 座位余量不足
     */
    SEAT_NOT_ENOUGH(1005,"座位余量不足"),

    /**
     * 拼车信息不存在
     */
    INFO_NOT_EXIST(1006,"拼车信息不存在"),

    /**
     * 订单不存在
     */
    ORDER_NOT_EXIST(1007,"订单不存在");

    private final int code;
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
