package com.ifugle.rap.model.shuixiaomi;

import java.sql.Timestamp;
import java.util.Date;

import com.ifugle.rap.core.model.impl.EnhanceModel;

public class KbsArticleDO extends EnhanceModel<Long> {
    private Long id;

    private Long parentId;

    private String areaId;

    private String articleType;

    private String bizType;

    private String category;

    private String keyword;

    private String title;

    private String remark;

    private Byte status;

    private Byte validFlag;

    private Date validDate;

    private Date invalidDate;

    private String original;

    private String issueNo;

    private String issueOrg;

    private Date issueDate;

    private String mapSource;

    private String mapId;

    private Integer syncFlag;

    private Timestamp syncTime;

    private String creator;

    private Date creationDate;

    private String modifier;

    private Date modificationDate;

    private String invalidReason;
    
    private Byte splitFlag;
    
    private Byte relationStatus;
    
    private Byte taxes;
    
    private Long oldArticleId;
    
    private Byte infoOpen;
    
    private String cityArea;
    
    private Long orgId;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType == null ? null : articleType.trim();
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
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

    public Byte getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Byte validFlag) {
        this.validFlag = validFlag;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Date getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(Date invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo == null ? null : issueNo.trim();
    }

    public String getIssueOrg() {
        return issueOrg;
    }

    public void setIssueOrg(String issueOrg) {
        this.issueOrg = issueOrg == null ? null : issueOrg.trim();
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
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

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason == null ? null : invalidReason.trim();
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }

	public Byte getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(Byte splitFlag) {
		this.splitFlag = splitFlag;
	}

	public Byte getRelationStatus() {
		return relationStatus;
	}

	public void setRelationStatus(Byte relationStatus) {
		this.relationStatus = relationStatus;
	}

	public Byte getTaxes() {
		return taxes;
	}

	public void setTaxes(Byte taxes) {
		this.taxes = taxes;
	}

	public Long getOldArticleId() {
		return oldArticleId;
	}

	public void setOldArticleId(Long oldArticleId) {
		this.oldArticleId = oldArticleId;
	}

	public Byte getInfoOpen() {
		return infoOpen;
	}

	public void setInfoOpen(Byte infoOpen) {
		this.infoOpen = infoOpen;
	}

	public String getCityArea() {
		return cityArea;
	}

	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "KbsArticleDO [id=" + id + ", parentId=" + parentId + ", areaId=" + areaId + ", articleType="
				+ articleType + ", bizType=" + bizType + ", category=" + category + ", keyword=" + keyword + ", title="
				+ title + ", remark=" + remark + ", status=" + status + ", validFlag=" + validFlag + ", validDate="
				+ validDate + ", invalidDate=" + invalidDate + ", original=" + original + ", issueNo=" + issueNo
				+ ", issueOrg=" + issueOrg + ", issueDate=" + issueDate + ", mapSource=" + mapSource + ", mapId="
				+ mapId + ", syncFlag=" + syncFlag + ", syncTime=" + syncTime + ", creator=" + creator
				+ ", creationDate=" + creationDate + ", modifier=" + modifier + ", modificationDate=" + modificationDate
				+ ", invalidReason=" + invalidReason + ", splitFlag=" + splitFlag + ", relationStatus=" + relationStatus
				+ ", taxes=" + taxes + ", oldArticleId=" + oldArticleId + ", infoOpen=" + infoOpen + ", cityArea="
				+ cityArea + ", orgId=" + orgId + "]";
	}
}
