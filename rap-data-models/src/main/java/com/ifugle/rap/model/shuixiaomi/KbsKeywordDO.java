package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class KbsKeywordDO extends EnhanceModel<Long> {
    private Long id;

    private Long orgId;

    private String keywordName;

    private String keywordPinyin;

    private String abbrev;

    private String synonyms;

    private String near;

    private String typos;

    private String hypernym;

    private String hyponym;

    private String related;

    private Byte domain;

    private Byte type;

    private String definition;

    private Integer score;

    private Integer weight;

    private Integer sensitivity;

    private Integer popularity;

    private Byte enabled;

    private Date creationDate;

    private String creator;

    private Date modificationDate;

    private String modifier;
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

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName == null ? null : keywordName.trim();
    }

    public String getKeywordPinyin() {
        return keywordPinyin;
    }

    public void setKeywordPinyin(String keywordPinyin) {
        this.keywordPinyin = keywordPinyin == null ? null : keywordPinyin.trim();
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev == null ? null : abbrev.trim();
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms == null ? null : synonyms.trim();
    }

    public String getNear() {
        return near;
    }

    public void setNear(String near) {
        this.near = near == null ? null : near.trim();
    }

    public String getTypos() {
        return typos;
    }

    public void setTypos(String typos) {
        this.typos = typos == null ? null : typos.trim();
    }

    public String getHypernym() {
        return hypernym;
    }

    public void setHypernym(String hypernym) {
        this.hypernym = hypernym == null ? null : hypernym.trim();
    }

    public String getHyponym() {
        return hyponym;
    }

    public void setHyponym(String hyponym) {
        this.hyponym = hyponym == null ? null : hyponym.trim();
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related == null ? null : related.trim();
    }

    public Byte getDomain() {
        return domain;
    }

    public void setDomain(Byte domain) {
        this.domain = domain;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition == null ? null : definition.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "KbsKeywordDO{" + "id=" + id + ", orgId=" + orgId + ", keywordName='" + keywordName + '\'' + ", keywordPinyin='" + keywordPinyin + '\''
                + ", abbrev='" + abbrev + '\'' + ", synonyms='" + synonyms + '\'' + ", near='" + near + '\'' + ", typos='" + typos + '\'' + ", hypernym='"
                + hypernym + '\'' + ", hyponym='" + hyponym + '\'' + ", related='" + related + '\'' + ", domain=" + domain + ", type=" + type + ", definition='"
                + definition + '\'' + ", score=" + score + ", weight=" + weight + ", sensitivity=" + sensitivity + ", popularity=" + popularity + ", enabled="
                + enabled + ", creationDate=" + creationDate + ", creator='" + creator + '\'' + ", modificationDate=" + modificationDate + ", modifier='"
                + modifier + '\'' + '}';
    }

    /***
     * 返回true，修改时间大于创建时间；反之返回false
     * @return
     */
    public boolean isNew() {
        return modificationDate.compareTo(creationDate) > 0 ? true : false;
    }
}
