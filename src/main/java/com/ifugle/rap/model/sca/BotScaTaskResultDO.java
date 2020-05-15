/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.sca;

import java.util.Date;

import com.ifugle.rap.core.model.impl.EnhanceModel;

/**
 * @author jwj
 * @version $Id$
 * @since May 13, 2020 8:43:00 PM
 */
public class BotScaTaskResultDO extends EnhanceModel<Long> {

	private static final long serialVersionUID = -5508634119719316178L;
	
	/** 主键ID */
	private Long id;
	/** 组织ID(=APP_DEPARTMENT.ID，仅组织根级部门) */
	private Long orgId;
	/** 用户ID */
	private Long userId;
	/** 语音文件集ID */
	private Long voiceSetId;
	/** 语音文件集名称 */
	private String voiceSetName;
	/** 语音文件ID */
	private Long voiceId;
	/** 语音文件名称 */
	private String voiceName;
	/** 得分，满分100。 */
	private Integer score;
	/** 任务批次ID */
	private Long batchTaskId;
	/** 阿里执行任务ID */
	private String taskId;
	/** 执行的状态，0：未执行，1：已执行 */
	private Integer execStatus;
	/** 命中的状态，0：未命中，1：已命中 */
	private Integer hitStatus;
	/** 命中规则ID集合，可以同步到ES上异构处理 */
	private String hitRuleIds;
	/** 分配的状态，0：未分配，1：已分配 */
	private Integer assignStatus;
	/** 复核的质检员 */
	private String resolver;
	/** 复核准确性；0：错误；1：正确。 */
	private Integer reviewResult;
	/** 复核状态；0：未复核；1：已复核。 */
	private Integer reviewStatus;
	/** 复核类型；0：自动复核；1：手动复核。 */
	private Integer reviewType;
	/** 复核意见 */
	private String comments;
	/** 有效(0=false, 1=true) */
	private Integer enabled;
	/** 备注 */
	private String remark;
	/** 创建日期 */
	private Date creationDate;
	/** 创建者 */
	private String creator;
	/** 修改日期 */
	private Date modificationDate;
	/** 修改人 */
	private String modifier;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getVoiceSetId() {
		return voiceSetId;
	}
	public void setVoiceSetId(Long voiceSetId) {
		this.voiceSetId = voiceSetId;
	}
	public String getVoiceSetName() {
		return voiceSetName;
	}
	public void setVoiceSetName(String voiceSetName) {
		this.voiceSetName = voiceSetName;
	}
	public Long getVoiceId() {
		return voiceId;
	}
	public void setVoiceId(Long voiceId) {
		this.voiceId = voiceId;
	}
	public String getVoiceName() {
		return voiceName;
	}
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Long getBatchTaskId() {
		return batchTaskId;
	}
	public void setBatchTaskId(Long batchTaskId) {
		this.batchTaskId = batchTaskId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Integer getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(Integer execStatus) {
		this.execStatus = execStatus;
	}
	public Integer getHitStatus() {
		return hitStatus;
	}
	public void setHitStatus(Integer hitStatus) {
		this.hitStatus = hitStatus;
	}
	public String getHitRuleIds() {
		return hitRuleIds;
	}
	public void setHitRuleIds(String hitRuleIds) {
		this.hitRuleIds = hitRuleIds;
	}
	public Integer getAssignStatus() {
		return assignStatus;
	}
	public void setAssignStatus(Integer assignStatus) {
		this.assignStatus = assignStatus;
	}
	public String getResolver() {
		return resolver;
	}
	public void setResolver(String resolver) {
		this.resolver = resolver;
	}
	public Integer getReviewResult() {
		return reviewResult;
	}
	public void setReviewResult(Integer reviewResult) {
		this.reviewResult = reviewResult;
	}
	public Integer getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Integer getReviewType() {
		return reviewType;
	}
	public void setReviewType(Integer reviewType) {
		this.reviewType = reviewType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}
