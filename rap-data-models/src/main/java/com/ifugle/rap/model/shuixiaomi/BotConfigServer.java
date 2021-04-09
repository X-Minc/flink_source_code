/**
 * Copyright(C) 2018 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.shuixiaomi;

import com.ifugle.rap.core.annotation.Label;
import com.ifugle.rap.core.model.impl.EnhanceModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @since 2018-01-19 13:41:28
 * @version $Id: BotConfigServer.java 90774 2019-01-10 12:55:25Z JiaoChao $
 * @author Yanlg
 */
public class BotConfigServer extends EnhanceModel<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * 类型(1=BEEBOT/云小蜜, 9=Class/内部服务, 30=WEB/网页, 31=DZSWJ/电子税务局, 40=MOBILE/移动网页, 50=APP/APP,
     * 51=SWAPP/税务APP, 70=DD/钉钉, 71=ZFB/支付宝, 72=WB/微博, 73=WX/微信, 74=QQ)
     */
    @NotNull
    @Label("类型")
    private Byte type;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 100)
    @Label("名称")
    private String name;

    /**
     * 简称
     */
    @NotNull
    @Size(max = 100)
    @Label("简称")
    private String shortName;

    /**
     * 继承服务器ID(0表示无继承)
     */
    @Label("继承服务器ID")
    private Long parentId;

    /**
     * 缺省节点ID(等同城市行政区划代码或其它)，预设值不能随意改动！
     */
    @NotNull
    @Label("缺省节点ID")
    private Integer defaultNodeId;

    /**
     * 仅组织根级部门
     */
    @NotNull
    @Label("组织ID")
    private Long orgId;

    /**
     * 是否允许游客模式：0-不允许；1-允许；
     */
    @Label("是否允许游客模式：0-不允许；1-允许；")
    private Byte allowGuest;

    /**
     * 认证厂商(支付宝：zfb；钉钉：dd；丁税宝：dsb；电子税务局：dzswj)
     */
    @Size(max = 10)
    @Label("认证厂商")
    private String authVendor;

    /**
     * 应用ID
     */
    @Label("应用ID")
    private Long appId;

    /**
     * 应用KEY
     */
    @Size(max = 128)
    @Label("应用KEY")
    private String appKey;

    /**
     * 应用密钥
     */
    @Size(max = 2048)
    @Label("应用密钥")
    private String appSecret;

    @Size(max = 200)
    @Label("访问token")
    private String accessToken;

    /**
     * 基础URL
     */
    @Size(max = 1024)
    @Label("基础URL")
    private String baseUrl;

    /**
     * 认证URL(若未配置则使用基础URL)
     */
    @Size(max = 1024)
    @Label("认证URL")
    private String authUrl;

    /**
     * 是否需要建立推荐关联（1=是，0=否）
     */
    @Label("是否需要建立推荐关联（1=是，0=否）")
    private Byte enabledForecast;

    /**
     * 消息级别 语聊(CHAT):0; 问题推荐(RECOMMEND):1; 命中问题(KNOWLEDGE):2; 多轮互动(BOT_ANSWER):3
     */
    @Label("消息级别 语聊")
    private Byte level;

    /**
     * 推荐类型消息提示
     */
    @Size(max = 2000)
    @Label("推荐类型消息提示")
    private String recommendTips;

    /**
     * 命中类型消息提示
     */
    @Size(max = 2000)
    @Label("命中类型消息提示")
    private String knowledgeTips;

    /**
     * 备注
     */
    @Size(max = 500)
    @Label("备注")
    private String remark;

    /**
     * 可视(0=false, 1=true)
     */
    @Label("可视")
    private Byte visible;

    /**
     * 有效(0=false, 1=true)
     */
    @Label("有效")
    private Byte enabled;

    private String convers;

    private Boolean hasEnglish;

    private Long nextServerId;

    private List<BotConfigServer> chatWindows;

    private String mobile;

    private Date approvalDate;

    private List<String> KbsCategoryNames;

    private String kbsType;

    private String creator;

    private String modifier;

    private Date modificationDate;

    public String getModifier() {
        return modifier;
    }

    public String getKbsType() {
        return kbsType;
    }

    public String getCreator() {
        return creator;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setKbsType(String kbsType) {
        this.kbsType = kbsType;
    }

    public List<String> getKbsCategoryNames() {
        return KbsCategoryNames;
    }

    public void setKbsCategoryNames(List<String> kbsCategoryNames) {
        KbsCategoryNames = kbsCategoryNames;
    }

    /**
     * @return the approvalDate
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * @param approvalDate the approvalDate to set
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the chatWindows
     */
    public List<BotConfigServer> getChatWindows() {
        return chatWindows;
    }

    /**
     * @param chatWindows the chatWindows to set
     */
    public void setChatWindows(List<BotConfigServer> chatWindows) {
        this.chatWindows = chatWindows;
    }

    /**
     * @return the nextServerId
     */
    public Long getNextServerId() {
        return nextServerId;
    }

    /**
     * @param nextServerId the nextServerId to set
     */
    public void setNextServerId(Long nextServerId) {
        this.nextServerId = nextServerId;
    }

    /**
     * @return the hasEnglish
     */
    public Boolean getHasEnglish() {
        return hasEnglish;
    }

    /**
     * @param hasEnglish the hasEnglish to set
     */
    public void setHasEnglish(Boolean hasEnglish) {
        this.hasEnglish = hasEnglish;
    }

    /**
     * @return the convers
     */
    public String getConvers() {
        return convers;
    }

    /**
     * @param convers the convers to set
     */
    public void setConvers(String convers) {
        this.convers = convers;
    }

    /**
     * 类型(1=BEEBOT/云小蜜, 9=Class/内部服务, 30=WEB/网页, 31=DZSWJ/电子税务局, 40=MOBILE/移动网页, 50=APP/APP,
     * 51=SWAPP/税务APP, 70=DD/钉钉, 71=ZFB/支付宝, 72=WB/微博, 73=WX/微信, 74=QQ)
     *
     * @return the type
     */
    public Byte getType() {
        return type;
    }

    /**
     * 类型(1=BEEBOT/云小蜜, 9=Class/内部服务, 30=WEB/网页, 31=DZSWJ/电子税务局, 40=MOBILE/移动网页, 50=APP/APP,
     * 51=SWAPP/税务APP, 70=DD/钉钉, 71=ZFB/支付宝, 72=WB/微博, 73=WX/微信, 74=QQ)
     *
     * @param type the type to set
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 名称
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = (name == null ? null : name.trim());
    }

    /**
     * 简称
     *
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 简称
     *
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = (shortName == null ? null : shortName.trim());
    }

    /**
     * 继承服务器ID(0表示无继承)
     *
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 继承服务器ID(0表示无继承)
     *
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 缺省节点ID(等同城市行政区划代码或其它)，预设值不能随意改动！
     *
     * @return the defaultNodeId
     */
    public Integer getDefaultNodeId() {
        return defaultNodeId;
    }

    /**
     * 缺省节点ID(等同城市行政区划代码或其它)，预设值不能随意改动！
     *
     * @param defaultNodeId the defaultNodeId to set
     */
    public void setDefaultNodeId(Integer defaultNodeId) {
        this.defaultNodeId = defaultNodeId;
    }

    /**
     * 是否允许游客模式：0-不允许；1-允许；
     *
     * @return the allowGuest
     */
    public Byte getAllowGuest() {
        return allowGuest;
    }

    /**
     * 是否允许游客模式：0-不允许；1-允许；
     *
     * @param allowGuest the allowGuest to set
     */
    public void setAllowGuest(Byte allowGuest) {
        this.allowGuest = allowGuest;
    }

    /**
     * 认证厂商(支付宝：zfb；钉钉：dd；丁税宝：dsb；电子税务局：dzswj)
     *
     * @return the authVendor
     */
    public String getAuthVendor() {
        return authVendor;
    }

    /**
     * 认证厂商(支付宝：zfb；钉钉：dd；丁税宝：dsb；电子税务局：dzswj)
     *
     * @param authVendor the authVendor to set
     */
    public void setAuthVendor(String authVendor) {
        this.authVendor = (authVendor == null ? null : authVendor.trim());
    }

    /**
     * 应用ID
     *
     * @return the appId
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * 应用ID
     *
     * @param appId the appId to set
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * 应用KEY
     *
     * @return the appKey
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * 应用KEY
     *
     * @param appKey the appKey to set
     */
    public void setAppKey(String appKey) {
        this.appKey = (appKey == null ? null : appKey.trim());
    }

    /**
     * 应用密钥
     *
     * @return the appSecret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * 应用密钥
     *
     * @param appSecret the appSecret to set
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = (appSecret == null ? null : appSecret.trim());
    }

    /**
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = (accessToken == null ? null : accessToken.trim());
    }

    /**
     * 基础URL
     *
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 基础URL
     *
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = (baseUrl == null ? null : baseUrl.trim());
    }

    /**
     * 认证URL(若未配置则使用基础URL)
     *
     * @return the authUrl
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * 认证URL(若未配置则使用基础URL)
     *
     * @param authUrl the authUrl to set
     */
    public void setAuthUrl(String authUrl) {
        this.authUrl = (authUrl == null ? null : authUrl.trim());
    }

    /**
     * 是否需要建立推荐关联（1=是，0=否）
     *
     * @return the enabledForecast
     */
    public Byte getEnabledForecast() {
        return enabledForecast;
    }

    /**
     * 是否需要建立推荐关联（1=是，0=否）
     *
     * @param enabledForecast the enabledForecast to set
     */
    public void setEnabledForecast(Byte enabledForecast) {
        this.enabledForecast = enabledForecast;
    }

    /**
     * 消息级别 语聊(CHAT):0; 问题推荐(RECOMMEND):1; 命中问题(KNOWLEDGE):2; 多轮互动(BOT_ANSWER):3
     *
     * @return the level
     */
    public Byte getLevel() {
        return level;
    }

    /**
     * 消息级别 语聊(CHAT):0; 问题推荐(RECOMMEND):1; 命中问题(KNOWLEDGE):2; 多轮互动(BOT_ANSWER):3
     *
     * @param level the level to set
     */
    public void setLevel(Byte level) {
        this.level = level;
    }

    /**
     * 推荐类型消息提示
     *
     * @return the recommendTips
     */
    public String getRecommendTips() {
        return recommendTips;
    }

    /**
     * 推荐类型消息提示
     *
     * @param recommendTips the recommendTips to set
     */
    public void setRecommendTips(String recommendTips) {
        this.recommendTips = (recommendTips == null ? null : recommendTips.trim());
    }

    /**
     * 命中类型消息提示
     *
     * @return the knowledgeTips
     */
    public String getKnowledgeTips() {
        return knowledgeTips;
    }

    /**
     * 命中类型消息提示
     *
     * @param knowledgeTips the knowledgeTips to set
     */
    public void setKnowledgeTips(String knowledgeTips) {
        this.knowledgeTips = (knowledgeTips == null ? null : knowledgeTips.trim());
    }

    /**
     * 备注
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     *
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = (remark == null ? null : remark.trim());
    }

    /**
     * 可视(0=false, 1=true)
     *
     * @return the visible
     */
    public Byte getVisible() {
        return visible;
    }

    /**
     * 可视(0=false, 1=true)
     *
     * @param visible the visible to set
     */
    public void setVisible(Byte visible) {
        this.visible = visible;
    }

    /**
     * 有效(0=false, 1=true)
     *
     * @return the enabled
     */
    public Byte getEnabled() {
        return enabled;
    }

    /**
     * 有效(0=false, 1=true)
     *
     * @param enabled the enabled to set
     */
    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public boolean isDeployInMobile() {
        if (this.getType().intValue() == 30) {
            return false;
        }

        return this.getType().intValue() != 31;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getDefaultNodeId() == null) ? 0 : getDefaultNodeId().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        result = prime * result + ((getAllowGuest() == null) ? 0 : getAllowGuest().hashCode());
        result = prime * result + ((getAuthVendor() == null) ? 0 : getAuthVendor().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getAppKey() == null) ? 0 : getAppKey().hashCode());
        result = prime * result + ((getAppSecret() == null) ? 0 : getAppSecret().hashCode());
        result = prime * result + ((getBaseUrl() == null) ? 0 : getBaseUrl().hashCode());
        result = prime * result + ((getAuthUrl() == null) ? 0 : getAuthUrl().hashCode());
        result = prime * result + ((getEnabledForecast() == null) ? 0 : getEnabledForecast().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getRecommendTips() == null) ? 0 : getRecommendTips().hashCode());
        result = prime * result + ((getKnowledgeTips() == null) ? 0 : getKnowledgeTips().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BotConfigServer other = (BotConfigServer)obj;
        return (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType())) && (
                this.getName() == null ? other.getName() == null : this.getName().equals(other.getName())) && (
                this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName())) && (this.getParentId() == null ?
                other.getParentId() == null :
                this.getParentId().equals(other.getParentId())) && (this.getDefaultNodeId() == null ?
                other.getDefaultNodeId() == null :
                this.getDefaultNodeId().equals(other.getDefaultNodeId())) && (this.getOrgId() == null ?
                other.getOrgId() == null :
                this.getOrgId().equals(other.getOrgId())) && (this.getAllowGuest() == null ?
                other.getAllowGuest() == null :
                this.getAllowGuest().equals(other.getAllowGuest())) && (this.getAuthVendor() == null ?
                other.getAuthVendor() == null :
                this.getAuthVendor().equals(other.getAuthVendor())) && (this.getAppId() == null ?
                other.getAppId() == null :
                this.getAppId().equals(other.getAppId())) && (this.getAppKey() == null ?
                other.getAppKey() == null :
                this.getAppKey().equals(other.getAppKey())) && (this.getAppSecret() == null ?
                other.getAppSecret() == null :
                this.getAppSecret().equals(other.getAppSecret())) && (this.getBaseUrl() == null ?
                other.getBaseUrl() == null :
                this.getBaseUrl().equals(other.getBaseUrl())) && (this.getAuthUrl() == null ?
                other.getAuthUrl() == null :
                this.getAuthUrl().equals(other.getAuthUrl())) && (this.getEnabledForecast() == null ?
                other.getEnabledForecast() == null :
                this.getEnabledForecast().equals(other.getEnabledForecast())) && (this.getLevel() == null ?
                other.getLevel() == null :
                this.getLevel().equals(other.getLevel())) && (this.getRecommendTips() == null ?
                other.getRecommendTips() == null :
                this.getRecommendTips().equals(other.getRecommendTips())) && (this.getKnowledgeTips() == null ?
                other.getKnowledgeTips() == null :
                this.getKnowledgeTips().equals(other.getKnowledgeTips())) && (this.getRemark() == null ?
                other.getRemark() == null :
                this.getRemark().equals(other.getRemark())) && (this.getVisible() == null ?
                other.getVisible() == null :
                this.getVisible().equals(other.getVisible())) && (this.getEnabled() == null ?
                other.getEnabled() == null :
                this.getEnabled().equals(other.getEnabled()));
    }

    /**
     * @return the orgId
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }


}

