package com.ifugle.rap.model.zhcs;

import java.util.Date;

public class ZxArticle {
    private Long id;

    private Long userId;

    private Integer nodeId;

    private Byte mapSource;

    private String title;

    private String author;

    private Long sortId;

    private String keyWord;

    private Byte publishType;

    private Byte coverType;

    private String content;

    private Byte allow;

    private Byte freeStat;

    private Date publishDate;

    private Byte enabled;

    private Date creationDate;

    private Date modificationDate;

    private Integer revision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Byte getMapSource() {
        return mapSource;
    }

    public void setMapSource(Byte mapSource) {
        this.mapSource = mapSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Long getSortId() {
        return sortId;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    public Byte getPublishType() {
        return publishType;
    }

    public void setPublishType(Byte publishType) {
        this.publishType = publishType;
    }

    public Byte getCoverType() {
        return coverType;
    }

    public void setCoverType(Byte coverType) {
        this.coverType = coverType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Byte getAllow() {
        return allow;
    }

    public void setAllow(Byte allow) {
        this.allow = allow;
    }

    public Byte getFreeStat() {
        return freeStat;
    }

    public void setFreeStat(Byte freeStat) {
        this.freeStat = freeStat;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
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

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }
}
