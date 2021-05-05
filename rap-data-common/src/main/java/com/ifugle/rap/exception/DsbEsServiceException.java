package com.ifugle.rap.exception;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import com.ifugle.rap.core.service.ServiceException;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:53
 */
public class DsbEsServiceException extends ServiceException {
    private static final long serialVersionUID = -3346320788123124279L;
    /**
     * 错误码
     */
    private Integer code;

    /**
     * @param msg
     */
    public DsbEsServiceException(String msg) {
        super(msg);
    }

    /**
     * @param msg
     * @param cause
     */
    public DsbEsServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * @param cause
     */
    public DsbEsServiceException(Throwable cause) {
        super(cause);
    }

    public DsbEsServiceException(String format, Object... argArray) {
        super(format, argArray);
    }

    public DsbEsServiceException(Integer code, String format, Object... argArray) {
        super(format, argArray);
        this.code = code;
    }

    public DsbEsServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public DsbEsServiceException(Throwable cause, String loggerMsgFormat, Object... argArray) {
        super(cause);
        final Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            if (null != argArray) {
                logger.error(MessageFormatter.arrayFormat(loggerMsgFormat, argArray).getMessage());
            } else {
                logger.error(loggerMsgFormat);
            }
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
