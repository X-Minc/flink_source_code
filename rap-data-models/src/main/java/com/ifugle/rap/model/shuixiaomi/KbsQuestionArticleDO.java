package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.sql.Timestamp;
import java.util.Date;

public class KbsQuestionArticleDO extends EnhanceModel<Long> {
    private Long id;

    private Long questionId;

    private Long articleId;

    private String articleType;

    private String articleTitle;

    private String remark;

    private Byte priority;

    private Byte status;

    private String mapSource;

    private String mapId;

    private Integer syncFlag;

    private Timestamp syncTime;

    private String creator;

    private Date creationDate;

    private String modifier;

    private Date modificationDate;

    private String articleBlock;
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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType == null ? null : articleType.trim();
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle == null ? null : articleTitle.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getPriority() {
        return priority;
    }

    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getMapSource() {
        return mapSource;
    }

    public void setMapSource(String mapSource) {
        this.mapSource = mapSource == null ? null : mapSource.trim();
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId == null ? null : mapId.trim();
    }

    public Integer getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Integer syncFlag) {
        this.syncFlag = syncFlag;
    }

    public Timestamp getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Timestamp syncTime) {
        this.syncTime = syncTime;
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

    public String getArticleBlock() {
        return articleBlock;
    }

    public void setArticleBlock(String articleBlock) {
        this.articleBlock = articleBlock == null ? null : articleBlock.trim();
    }

    @Override
    public String toString() {
        return "KbsQuestionArticleDO{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", articleId=" + articleId +
                ", articleType='" + articleType + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", remark='" + remark + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", mapSource='" + mapSource + '\'' +
                ", mapId='" + mapId + '\'' +
                ", syncFlag=" + syncFlag +
                ", syncTime=" + syncTime +
                ", creator='" + creator + '\'' +
                ", creationDate=" + creationDate +
                ", modifier='" + modifier + '\'' +
                ", modificationDate=" + modificationDate +
                ", articleBlock='" + articleBlock + '\'' +
                '}';
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }
}
