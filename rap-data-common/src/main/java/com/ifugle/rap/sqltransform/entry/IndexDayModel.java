package com.ifugle.rap.sqltransform.entry;

/**
 * @author Minc
 * @date 2022/1/4 11:07
 */
public class IndexDayModel implements Cloneable{

    //统计日期
    private Integer cycleId;
    //节点ID(同统计日期)Partition(hash())
    private Integer nodeId;
    //虚拟组织ID，跨组织汇总组织ID为0
    private Long orgId;
    //指标代码
    private String index;
    //维度代码1
    private String dim1;
    //维度数据代码1
    private String dimData1;
    //维度代码2
    private String dim2;
    //维度数据代码2
    private String dimData2;
    //维度代码3
    private String dim3;
    //维度数据代码3
    private String dimData3;
    //增加数
    private Integer incCount = 0;
    //增加数,按照增量标签汇总
    private Integer inc2Count = 0;
    //净增数
    private Integer netIncCount = 0;
    //减少数
    private Integer decCount = 0;
    //累计数
    private Integer totalCount = 0;
    //增加金额
    private Double incAmount = 0.0;
    //净增金额
    private Double netIncAmount = 0.0;
    //减少金额
    private Double decAmount = 0.0;
    //累计金额
    private Double totalAmount = 0.0;
    //同比增加数
    private Double incCountChainRat = 0.0;
    //环比增加数
    private Double incCountLinkRat = 0.0;
    //同比净增数
    private Double netIncCountChainRat = 0.0;
    //环比净增数
    private Double netIncCountLinkRat = 0.0;
    //同比减少数
    private Double decCountChainRat = 0.0;
    //环比减少数
    private Double decCountLinkRat = 0.0;
    //同比累计数
    private Double totalCountChainRat = 0.0;
    //环比累计数
    private Double totalCountLinkRat = 0.0;
    //累计数占比
    private Double totalCountShareRat = 0.0;
    //同比增加金额
    private Double incAmountChainRat = 0.0;
    //环比增加金额
    private Double incAmountLinkRat = 0.0;
    //同比净增金额
    private Double netIncAmountChainRat = 0.0;
    //环比净增金额
    private Double netIncAmountLinkRat = 0.0;
    //同比减少金额
    private Double decAmountChainRat = 0.0;
    //环比减少金额
    private Double decAmountLinkRat = 0.0;
    //同比累计金额
    private Double totalAmountChainRat = 0.0;
    //环比累计金额
    private Double totalAmountLinkRat = 0.0;
    //累计金额占比
    private Double totalAmountShareRat = 0.0;

    /*********************************************************************************************************************
     get,set
     *********************************************************************************************************************/
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDim1() {
        return dim1;
    }

    public void setDim1(String dim1) {
        this.dim1 = dim1;
    }

    public String getDimData1() {
        return dimData1;
    }

    public void setDimData1(String dimData1) {
        this.dimData1 = dimData1;
    }

    public String getDim2() {
        return dim2;
    }

    public void setDim2(String dim2) {
        this.dim2 = dim2;
    }

    public String getDimData2() {
        return dimData2;
    }

    public void setDimData2(String dimData2) {
        this.dimData2 = dimData2;
    }

    public String getDim3() {
        return dim3;
    }

    public void setDim3(String dim3) {
        this.dim3 = dim3;
    }

    public String getDimData3() {
        return dimData3;
    }

    public void setDimData3(String dimData3) {
        this.dimData3 = dimData3;
    }

    public Integer getIncCount() {
        return incCount;
    }

    public void setIncCount(Integer incCount) {
        this.incCount = incCount;
    }

    public Integer getInc2Count() {
        return inc2Count;
    }

    public void setInc2Count(Integer inc2Count) {
        this.inc2Count = inc2Count;
    }

    public Integer getNetIncCount() {
        return netIncCount;
    }

    public void setNetIncCount(Integer netIncCount) {
        this.netIncCount = netIncCount;
    }

    public Integer getDecCount() {
        return decCount;
    }

    public void setDecCount(Integer decCount) {
        this.decCount = decCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getIncAmount() {
        return incAmount;
    }

    public void setIncAmount(Double incAmount) {
        this.incAmount = incAmount;
    }

    public Double getNetIncAmount() {
        return netIncAmount;
    }

    public void setNetIncAmount(Double netIncAmount) {
        this.netIncAmount = netIncAmount;
    }

    public Double getDecAmount() {
        return decAmount;
    }

    public void setDecAmount(Double decAmount) {
        this.decAmount = decAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getIncCountChainRat() {
        return incCountChainRat;
    }

    public void setIncCountChainRat(Double incCountChainRat) {
        this.incCountChainRat = incCountChainRat;
    }

    public Double getIncCountLinkRat() {
        return incCountLinkRat;
    }

    public void setIncCountLinkRat(Double incCountLinkRat) {
        this.incCountLinkRat = incCountLinkRat;
    }

    public Double getNetIncCountChainRat() {
        return netIncCountChainRat;
    }

    public void setNetIncCountChainRat(Double netIncCountChainRat) {
        this.netIncCountChainRat = netIncCountChainRat;
    }

    public Double getNetIncCountLinkRat() {
        return netIncCountLinkRat;
    }

    public void setNetIncCountLinkRat(Double netIncCountLinkRat) {
        this.netIncCountLinkRat = netIncCountLinkRat;
    }

    public Double getDecCountChainRat() {
        return decCountChainRat;
    }

    public void setDecCountChainRat(Double decCountChainRat) {
        this.decCountChainRat = decCountChainRat;
    }

    public Double getDecCountLinkRat() {
        return decCountLinkRat;
    }

    public void setDecCountLinkRat(Double decCountLinkRat) {
        this.decCountLinkRat = decCountLinkRat;
    }

    public Double getTotalCountChainRat() {
        return totalCountChainRat;
    }

    public void setTotalCountChainRat(Double totalCountChainRat) {
        this.totalCountChainRat = totalCountChainRat;
    }

    public Double getTotalCountLinkRat() {
        return totalCountLinkRat;
    }

    public void setTotalCountLinkRat(Double totalCountLinkRat) {
        this.totalCountLinkRat = totalCountLinkRat;
    }

    public Double getTotalCountShareRat() {
        return totalCountShareRat;
    }

    public void setTotalCountShareRat(Double totalCountShareRat) {
        this.totalCountShareRat = totalCountShareRat;
    }

    public Double getIncAmountChainRat() {
        return incAmountChainRat;
    }

    public void setIncAmountChainRat(Double incAmountChainRat) {
        this.incAmountChainRat = incAmountChainRat;
    }

    public Double getIncAmountLinkRat() {
        return incAmountLinkRat;
    }

    public void setIncAmountLinkRat(Double incAmountLinkRat) {
        this.incAmountLinkRat = incAmountLinkRat;
    }

    public Double getNetIncAmountChainRat() {
        return netIncAmountChainRat;
    }

    public void setNetIncAmountChainRat(Double netIncAmountChainRat) {
        this.netIncAmountChainRat = netIncAmountChainRat;
    }

    public Double getNetIncAmountLinkRat() {
        return netIncAmountLinkRat;
    }

    public void setNetIncAmountLinkRat(Double netIncAmountLinkRat) {
        this.netIncAmountLinkRat = netIncAmountLinkRat;
    }

    public Double getDecAmountChainRat() {
        return decAmountChainRat;
    }

    public void setDecAmountChainRat(Double decAmountChainRat) {
        this.decAmountChainRat = decAmountChainRat;
    }

    public Double getDecAmountLinkRat() {
        return decAmountLinkRat;
    }

    public void setDecAmountLinkRat(Double decAmountLinkRat) {
        this.decAmountLinkRat = decAmountLinkRat;
    }

    public Double getTotalAmountChainRat() {
        return totalAmountChainRat;
    }

    public void setTotalAmountChainRat(Double totalAmountChainRat) {
        this.totalAmountChainRat = totalAmountChainRat;
    }

    public Double getTotalAmountLinkRat() {
        return totalAmountLinkRat;
    }

    public void setTotalAmountLinkRat(Double totalAmountLinkRat) {
        this.totalAmountLinkRat = totalAmountLinkRat;
    }

    public Double getTotalAmountShareRat() {
        return totalAmountShareRat;
    }

    public void setTotalAmountShareRat(Double totalAmountShareRat) {
        this.totalAmountShareRat = totalAmountShareRat;
    }

    @Override
    public IndexDayModel clone() {
        try {
            IndexDayModel clone = (IndexDayModel) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
