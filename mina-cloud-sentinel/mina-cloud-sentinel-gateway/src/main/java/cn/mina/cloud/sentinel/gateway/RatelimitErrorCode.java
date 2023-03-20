package cn.mina.cloud.sentinel.gateway;

import cn.mina.boot.common.exception.ErrorCode;

/**
 * @author Created by haoteng on 2022/7/19.
 */
public enum RatelimitErrorCode implements ErrorCode {
    // 触发限流
    ERROR_RATELIMIT_BLOCK_ERROR(700000, "系统繁忙，请稍后再试"),
    // 触发降级
    ERROR_RATELIMIT_DEGRADE_ERROR(710000, "系统繁忙，请稍后再试")
    ;

    private int code;

    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    RatelimitErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public ErrorCode customCode(int code) {
        this.code = code;
        return this;
    }

    public ErrorCode customMassage(String message) {
        this.message = message;
        return this;
    }

    public ErrorCode customCodeAndMassage(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }
}
