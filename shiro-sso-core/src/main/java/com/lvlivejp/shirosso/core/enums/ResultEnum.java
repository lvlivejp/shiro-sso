package com.lvlivejp.shirosso.core.enums;

public enum ResultEnum {
    SUCCESS("0000", "成功"),
    FAIL("9999", "失败"),
    REPAET("9001", "重复秒杀"),
    SECKILL_CLOSE("9002", "无库存"),
    UNLOGIN("9101", "未登录"),
    UNKNOWN_ACCOUNT("9102", "无此用户"),
    INCORRECT_CREDENTIALS("9103", "密码错误"),
    UNAUTHORIZED("9104", "未授权"),
    ;

    /**
     * 代码
     **/
    private String code;

    /**
     * 名称
     **/
    private String name;

    ResultEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
