package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class KbsReadingDO extends EnhanceModel<Long> {
    private Long id;

    private String keyword;

    private String synonyms;

    private String title;

    private String remark;

    private Byte status;

    private String original;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms == null ? null : synonyms.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
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

    @Override
    public String toString() {
        return "KbsReadingDO{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", synonyms='" + synonyms + '\'' +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", original='" + original + '\'' +
                ", creator='" + creator + '\'' +
                ", creationDate=" + creationDate +
                ", modifier='" + modifier + '\'' +
                ", modificationDate=" + modificationDate +
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
