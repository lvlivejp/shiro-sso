package com.lvlivejp.shirosso.exceptionHandle;

import com.lvlivejp.shirosso.base.BaseResult;
import com.lvlivejp.shirosso.enums.ResultEnum;
import com.lvlivejp.shirosso.exception.BusinessException;
import com.lvlivejp.shirosso.utils.BaseResultUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class SecKillExceptionHandle {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(BusinessException.class)
    public BaseResult processBusinessException(Exception e) {
        logger.error(e.getMessage(), e);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return BaseResultUtils.fail(businessException.getErrorCode(), businessException.getMessage());
        }
        return BaseResultUtils.fail(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName());
    }

    @ExceptionHandler(ShiroException.class)
    public BaseResult processCredentialsException(Exception e) {
        if (e instanceof IncorrectCredentialsException) {
            return BaseResultUtils.fail(ResultEnum.INCORRECT_CREDENTIALS.getCode(), ResultEnum.INCORRECT_CREDENTIALS.getName());
        } else if (e instanceof UnknownAccountException) {
            return BaseResultUtils.fail(ResultEnum.UNKNOWN_ACCOUNT.getCode(), ResultEnum.UNKNOWN_ACCOUNT.getName());
        }
        logger.error(e.getMessage(), e);
        return BaseResultUtils.fail(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName());
    }
}
