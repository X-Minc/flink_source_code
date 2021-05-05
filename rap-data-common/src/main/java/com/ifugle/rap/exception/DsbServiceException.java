package com.ifugle.rap.exception;

import com.ifugle.util.NestedRuntimeException;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:56
 */
public class DsbServiceException extends NestedRuntimeException {
    public DsbServiceException(String msg) {
        super(msg);
    }

    public DsbServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DsbServiceException(Throwable cause) {
        super(cause);
    }

    public DsbServiceException(String format, Object... argArray) {
        super(format, argArray);
    }
}
