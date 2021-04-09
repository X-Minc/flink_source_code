package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class BotMediaDO extends EnhanceModel<Long> {
    private Long id;

    private Integer nodeId;

    private Long requestId;

    private Long responseMessageId;

    private Long orgId;

    private Long serverId;

    private String fileName;

    private Byte fileType;

    private Long fileSize;

    private String recognitionId;

    private String recognitionText;

    private String correctionText;

    private Date correctionDate;

    private String ossKey;

    private String mediaId;

    private Byte mediaType;

    private Integer duration;

    private Date creationDate;

    private String mediaSource;

    private Long status;

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

    public Long getRequestId() {
        return requestId;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long mediaStatus) {
        this.status = mediaStatus;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(Long responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Byte getFileType() {
        return fileType;
    }

    public void setFileType(Byte fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRecognitionId() {
        return recognitionId;
    }

    public void setRecognitionId(String recognitionId) {
        this.recognitionId = recognitionId == null ? null : recognitionId.trim();
    }

    public String getRecognitionText() {
        return recognitionText;
    }

    public void setRecognitionText(String recognitionText) {
        this.recognitionText = recognitionText == null ? null : recognitionText.trim();
    }

    public String getCorrectionText() {
        return correctionText;
    }

    public void setCorrectionText(String correctionText) {
        this.correctionText = correctionText == null ? null : correctionText.trim();
    }

    public Date getCorrectionDate() {
        return correctionDate;
    }

    public void setCorrectionDate(Date correctionDate) {
        this.correctionDate = correctionDate;
    }

    public String getOssKey() {
        return ossKey;
    }

    public void setOssKey(String ossKey) {
        this.ossKey = ossKey == null ? null : ossKey.trim();
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId == null ? null : mediaId.trim();
    }

    public Byte getMediaType() {
        return mediaType;
    }

    public void setMediaType(Byte mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
