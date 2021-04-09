package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class BotChatResponseMessageDO extends EnhanceModel<Long> {
    private Long id;

    private Integer nodeId;

    private Long requestId;

    private Long responseId;

    private Long userId;

    private String messageTip;

    private Byte arrayIndex;

    private Byte subIndex;

    private String type;

    private String title;

    private String summary;

    private String content;

    private String knowledgeId;

    private String answerSource;

    private Byte selected;

    private Date clickedTime;

    private Date creationDate;

    private Integer duration;

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

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessageTip() {
        return messageTip;
    }

    public void setMessageTip(String messageTip) {
        this.messageTip = messageTip == null ? null : messageTip.trim();
    }

    public Byte getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(Byte arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public Byte getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(Byte subIndex) {
        this.subIndex = subIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId == null ? null : knowledgeId.trim();
    }

    public String getAnswerSource() {
        return answerSource;
    }

    public void setAnswerSource(String answerSource) {
        this.answerSource = answerSource == null ? null : answerSource.trim();
    }

    public Byte getSelected() {
        return selected;
    }

    public void setSelected(Byte selected) {
        this.selected = selected;
    }

    public Date getClickedTime() {
        return clickedTime;
    }

    public void setClickedTime(Date clickedTime) {
        this.clickedTime = clickedTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "BotChatResponseMessageDO{" +
                "id=" + id +
                ", nodeId=" + nodeId +
                ", requestId=" + requestId +
                ", responseId=" + responseId +
                ", userId=" + userId +
                ", messageTip='" + messageTip + '\'' +
                ", arrayIndex=" + arrayIndex +
                ", subIndex=" + subIndex +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", knowledgeId='" + knowledgeId + '\'' +
                ", answerSource='" + answerSource + '\'' +
                ", selected=" + selected +
                ", clickedTime=" + clickedTime +
                ", creationDate=" + creationDate +
                ", duration=" + duration +
                '}';
    }
}
