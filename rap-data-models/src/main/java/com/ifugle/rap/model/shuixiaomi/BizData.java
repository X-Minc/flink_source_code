package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class BizData extends EnhanceModel<Long> {
    private Long id;

    private Integer nodeId;

    private Long orgId;

    private Long defineId;

    private String id1;

    private String id2;

    private String id3;

    private String data1;

    private String data2;

    private String data3;

    private String data4;

    private String data5;

    private Double latitude;

    private Double longitude;

    private Byte status;

    private String creator;

    private Date creationDate;

    private String modifier;

    private Date modificationDate;

    /***
     * 判断是新增还是更新
     */
    private boolean isNew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDefineId() {
        return defineId;
    }

    public void setDefineId(Long defineId) {
        this.defineId = defineId;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1 == null ? null : id1.trim();
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2 == null ? null : id2.trim();
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3 == null ? null : id3.trim();
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1 == null ? null : data1.trim();
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2 == null ? null : data2.trim();
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3 == null ? null : data3.trim();
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4 == null ? null : data4.trim();
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5 == null ? null : data5.trim();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }
}
