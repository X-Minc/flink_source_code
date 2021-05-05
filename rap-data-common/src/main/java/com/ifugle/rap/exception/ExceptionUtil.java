package com.ifugle.rap.exception;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:07
 */
public class ExceptionUtil {
    public ExceptionUtil() {
    }

    public static String getExceptionDetail(Exception e) {
        StringBuffer stringBuffer = new StringBuffer("\n" + e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        int length = messages.length;

        for (int i = 0; i < length; ++i) {
            stringBuffer.append("\t" + messages[i].toString() + "\n");
        }

        return stringBuffer.toString();
    }
}