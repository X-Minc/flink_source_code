package com.ifugle.rap.model.shuixiaomi;


import java.sql.Timestamp;
import java.util.Date;

import com.ifugle.rap.core.model.impl.EnhanceModel;

public class KbsQuestionDO extends EnhanceModel<Long> {
    private Long id;

    private String question;

    private String questionType;

    private Byte grade;

    private String category;

    private String primaryKeyword;

    private String alternateKeyword;

    private String searchKeyword;

    private Byte keywordOption;

    private String synonyms;

    private Integer answerShape;

    private String answer;

    private String remark;

    private Byte status;

    private Date validDate;

    private Date invalidDate;

    private String invalidReason;

    private Byte addMode;

    private String original;

    private String mapSource;

    private String mapId;

    private Integer syncFlag;

    private Timestamp syncTime;

    private String creator;

    private Date creationDate;

    private String modifier;

    private Date modificationDate;

    private Byte approvalStatus;

    private String answerMD5;

    private String voiceAnswer;

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType == null ? null : questionType.trim();
    }

    public Integer getAnswerShape() {
        return answerShape;
    }

    public void setAnswerShape(Integer answerShape) {
        this.answerShape = answerShape;
    }

    public Byte getGrade() {
        return grade;
    }

    public void setGrade(Byte grade) {
        this.grade = grade;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getPrimaryKeyword() {
        return primaryKeyword;
    }

    public void setPrimaryKeyword(String primaryKeyword) {
        this.primaryKeyword = primaryKeyword == null ? null : primaryKeyword.trim();
    }

    public String getAlternateKeyword() {
        return alternateKeyword;
    }

    public void setAlternateKeyword(String alternateKeyword) {
        this.alternateKeyword = alternateKeyword == null ? null : alternateKeyword.trim();
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword == null ? null : searchKeyword.trim();
    }

    public Byte getKeywordOption() {
        return keywordOption;
    }

    public void setKeywordOption(Byte keywordOption) {
        this.keywordOption = keywordOption;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms == null ? null : synonyms.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
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

    public Byte getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Byte approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Byte getAddMode() {
        return addMode;
    }

    public void setAddMode(Byte addMode) {
        this.addMode = addMode;
    }


    public String getAnswerMD5() {
        return answerMD5;
    }

    public void setAnswerMD5(String answerMD5) {
        this.answerMD5 = answerMD5;
    }

    public String getVoiceAnswer() {
        return voiceAnswer;
    }

    public void setVoiceAnswer(String voiceAnswer) {
        this.voiceAnswer = voiceAnswer;
    }

    @Override
    public String toString() {
        return "KbsQuestionDO{" + "id=" + id + ", question='" + question + '\'' + ", questionType='" + questionType + '\'' + ", grade=" + grade + ", category='"
                + category + '\'' + ", primaryKeyword='" + primaryKeyword + '\'' + ", alternateKeyword='" + alternateKeyword + '\'' + ", searchKeyword='"
                + searchKeyword + '\'' + ", keywordOption=" + keywordOption + ", synonyms='" + synonyms + '\'' + ", answer='" + answer + '\'' + ", remark='"
                + remark + '\'' + ", status=" + status + ", validDate=" + validDate + ", invalidDate=" + invalidDate + ", invalidReason='" + invalidReason
                + '\'' + ", original='" + original + '\'' + ", mapSource='" + mapSource + '\'' + ", mapId='" + mapId + '\'' + ", syncFlag=" + syncFlag
                + ", syncTime=" + syncTime + ", creator='" + creator + '\'' + ", creationDate=" + creationDate + ", modifier='" + modifier + '\''
                + ", modificationDate=" + modificationDate + ", approvalStatus=" + approvalStatus + '}';
    }


    public String toString2() {
        return "KbsQuestionDO{" + "id=" + id + ", validDate=" + validDate + ", invalidDate=" + invalidDate + ", invalidReason='" + invalidReason + '\''
                + ", approvalStatus=" + approvalStatus + '}';
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }
}
