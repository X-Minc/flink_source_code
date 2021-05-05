package com.ifugle.rap.exception;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import com.ifugle.rap.core.service.ServiceException;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:52
 */
public class DsbEsRemoteException extends ServiceException {
    private static final long serialVersionUID = -324567888474459933L;
    /**
     * 错误码
     */
    private Integer code;

    /**
     * @param msg
     */
    public DsbEsRemoteException(String msg) {
        super(msg);
    }

    /**
     * @param msg
     * @param cause
     */
    public DsbEsRemoteException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * @param cause
     */
    public DsbEsRemoteException(Throwable cause) {
        super(cause);
    }

    public DsbEsRemoteException(String format, Object... argArray) {
        super(format, argArray);
    }

    public DsbEsRemoteException(Integer code, String format, Object... argArray) {
        super(format, argArray);
        this.code = code;
    }

    public DsbEsRemoteException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public DsbEsRemoteException(Throwable cause, String loggerMsgFormat, Object... argArray) {
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
