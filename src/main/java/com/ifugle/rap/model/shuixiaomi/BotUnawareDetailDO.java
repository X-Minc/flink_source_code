package com.ifugle.rap.model.shuixiaomi;

import java.util.Date;

import com.ifugle.rap.core.model.impl.EnhanceModel;

public class BotUnawareDetailDO extends EnhanceModel<Long> {

    private Long id;
    private Integer nodeId;
    private Long serverId;
    private String serverName;
    private Long userId;
    private Long requestId;
    private String authVendor;
    private String messageId;
    private String utterance;
    private String trackCode;
    private Byte type;
    private Byte satisfactionLevel;
    private String category;
    private Byte priority;
    private Long clusterId;
    private String tags;
    private String reason;
    private String status;
    private String handler;
    private Date handleDate;
    private String remark;
    private Date creationDate;
    private Date modificationDate;
    private Long deptId;
    private String companyName;
    /***
     * 判断是新增还是更新
     */
    private boolean isNew;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

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

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName == null ? null : serverName.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAuthVendor() {
        return authVendor;
    }

    public void setAuthVendor(String authVendor) {
        this.authVendor = authVendor == null ? null : authVendor.trim();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance == null ? null : utterance.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(Byte satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Byte getPriority() {
        return priority;
    }

    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler == null ? null : handler.trim();
    }

    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "BotUnawareDetailDO{" + "companyName=" + companyName + ", id=" + id + ", nodeId=" + nodeId + ", serverId=" + serverId
                + ", serverName='" + serverName + '\'' + ", userId=" + userId + ", requestId="
                + requestId + ", authVendor='" + authVendor + '\'' + ", messageId='" + messageId
                + '\'' + ", utterance='" + utterance + '\'' + ", trackCode='" + trackCode + '\''
                + ", type=" + type + ", satisfactionLevel=" + satisfactionLevel + ", category='"
                + category + '\'' + ", priority=" + priority + ", clusterId=" + clusterId
                + ", tags='" + tags + '\'' + ", reason='" + reason + '\'' + ", status='" + status
                + '\'' + ", handler='" + handler + '\'' + ", handleDate=" + handleDate
                + ", remark='" + remark + '\'' + ", creationDate=" + creationDate
                + ", modificationDate=" + modificationDate + ", deptId='" + deptId + '\'' + '}';
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }
}
