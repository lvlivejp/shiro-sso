package com.lvlivejp.shirosso.core.base;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseResult<T> {

    /**
     * 代码
     **/
    public String code;

    /**
     * 错误信息
     **/
    public String message;

    public T t;

}
