package com.ifugle.rap.model.sca;

import java.io.Serializable;

/**
 * @author ChenXiaolong
 * @version :$
 * @since 2020/4/28 19:36
 */
public class BotScaRuleHitStatus implements Serializable {

    private static final long serialVersionUID = -68436362160771526L;

    /**
     * 对应规则表里面的rid也就是阿里云同步的ID
     */
    private String rid;

    /**
     *  规则名称
     */
    private String rname;

    /**
     * 规则命中情况 0：未命中，1：命中， 3：未复核
     */
    private Integer hitStatus;

    public BotScaRuleHitStatus(String rid, String rname, Integer hitStatus) {
        this.rid = rid;
        this.rname = rname;
        this.hitStatus = hitStatus;
    }

    public BotScaRuleHitStatus() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public Integer getHitStatus() {
        return hitStatus;
    }

    public void setHitStatus(Integer hitStatus) {
        this.hitStatus = hitStatus;
    }
}
