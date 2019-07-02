/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.elasticsearch.model;

/***
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: BizException.java,
 *  v 0.1 2018年5月15日 下午2:44:04 HuangLei(wenyuan) Exp $
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -238091758285157331L;

    private int               errCode;
    private String            errMsg;
    private int               subErrCode;
    private String            subErrMsg;

    public BizException() {
        super();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(int errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BizException(int errCode, String errMsg, int subErrCode, String subErrMsg) {
        super(errCode + ":" + errMsg + ":" + subErrCode + ":" + subErrMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.subErrCode = subErrCode;
        this.subErrMsg = subErrMsg;
    }

    public int getSubErrCode() {
        return subErrCode;
    }

    public void setSubErrCode(int subErrCode) {
        this.subErrCode = subErrCode;
    }

    public String getSubErrMsg() {
        return subErrMsg;
    }

    public void setSubErrMsg(String subErrMsg) {
        this.subErrMsg = subErrMsg;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }
}
