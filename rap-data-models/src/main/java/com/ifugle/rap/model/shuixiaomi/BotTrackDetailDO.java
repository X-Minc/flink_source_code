package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class BotTrackDetailDO extends EnhanceModel<Long> {
    private Long id;

    private Integer nodeId;

    private Long userId;

    private String userName;

    private Long sessionId;

    private Long requestId;

    private String messageId;

    private Long serverId;

    private String serverName;

    private String authVendor;

    private String trackCode;

    private String trackData;

    private String remark;

    private Date creationDate;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
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

    public String getAuthVendor() {
        return authVendor;
    }

    public void setAuthVendor(String authVendor) {
        this.authVendor = authVendor == null ? null : authVendor.trim();
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode == null ? null : trackCode.trim();
    }

    public String getTrackData() {
        return trackData;
    }

    public void setTrackData(String trackData) {
        this.trackData = trackData == null ? null : trackData.trim();
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

    @Override
    public String toString() {
        return "BotTrackDetailDO{" +
                "id=" + id +
                ", nodeId=" + nodeId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", sessionId=" + sessionId +
                ", requestId=" + requestId +
                ", messageId='" + messageId + '\'' +
                ", serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", authVendor='" + authVendor + '\'' +
                ", trackCode='" + trackCode + '\'' +
                ", trackData='" + trackData + '\'' +
                ", remark='" + remark + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
