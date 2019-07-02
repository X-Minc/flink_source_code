/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.elasticsearch;

import java.util.Map;

/**
 * @author LiuZhengyang
 * @version $Id: InstanceErrorDTO.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月18日 11:07
 */
public class InstanceErrorDTO {

    private String type;

    private String reason;

    private Map<String,String> caused_by;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String,String> getCaused_by() {
        return caused_by;
    }

    public void setCaused_by(Map<String,String> caused_by) {
        this.caused_by = caused_by;
    }

    @Override
    public String toString() {
        return "InstanceErrorDTO{" + "type='" + type + '\'' + ", reason='" + reason + '\'' + ", caused_by=" + caused_by + '}';
    }
}
