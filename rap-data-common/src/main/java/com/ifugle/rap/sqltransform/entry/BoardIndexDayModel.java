package com.ifugle.rap.sqltransform.entry;

/**
 * @author Minc
 * @date 2022/1/11 10:46
 */
public class BoardIndexDayModel {
    private Integer cycleId;
    private Integer nodeId;
    private Long xnzzId;
    private String swjg_dm;
    private Double userActiveRate;
    private Double nsrActiveRate;
    private Double nsrActiveRateIndividual;
    private Double nsrActiveRateQy;

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Long getXnzzId() {
        return xnzzId;
    }

    public void setXnzzId(Long xnzzId) {
        this.xnzzId = xnzzId;
    }

    public String getSwjg_dm() {
        return swjg_dm;
    }

    public void setSwjg_dm(String swjg_dm) {
        this.swjg_dm = swjg_dm;
    }

    public Double getUserActiveRate() {
        return userActiveRate;
    }

    public void setUserActiveRate(Double userActiveRate) {
        this.userActiveRate = userActiveRate;
    }

    public Double getNsrActiveRate() {
        return nsrActiveRate;
    }

    public void setNsrActiveRate(Double nsrActiveRate) {
        this.nsrActiveRate = nsrActiveRate;
    }

    public Double getNsrActiveRateIndividual() {
        return nsrActiveRateIndividual;
    }

    public void setNsrActiveRateIndividual(Double nsrActiveRateIndividual) {
        this.nsrActiveRateIndividual = nsrActiveRateIndividual;
    }

    public Double getNsrActiveRateQy() {
        return nsrActiveRateQy;
    }

    public void setNsrActiveRateQy(Double nsrActiveRateQy) {
        this.nsrActiveRateQy = nsrActiveRateQy;
    }
}
