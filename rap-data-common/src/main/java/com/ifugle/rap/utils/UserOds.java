package com.ifugle.rap.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月23日 13:56
 */
@Data
public class UserOds implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("yh_nsr_id")
    private Long yhNsrId;

    @SerializedName("xnzz_id")
    private Long xnzzId;

    @SerializedName("bm_id")
    private Long bmId;

    /**
     * 用户类型 0:中间表用户，1:丁税宝用户
     */
    @SerializedName("yh_type")
    private Byte yhType;

    @SerializedName("yh_id")
    private Long yhId;

    @SerializedName("nsr_id")
    private Long nsrId;

    /**
     * 部门集合 所有所属上级部门和本级部门集合
     */
    @SerializedName("bm_ids")
    private List<Long> bmIds;

    /**
     * 用户成员状态
     */
    @SerializedName("yh_cyzt")
    private Byte yhCyzt;

    /**
     * 用户激活时间 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("yh_jhsj")
    private Date yhJhsj;

    @SerializedName("yh_zcfsbj")
    private Byte yhZcfsbj;

    @SerializedName("yh_zcrzjb")
    private Byte yhZcrzjb;

    /**
     * 用户删除标记
     */
    @SerializedName("yh_is_delete")
    private Boolean yhIsDelete;

    @SerializedName("yh_cjsj")
    private Date yhCjsj;

    @SerializedName("yh_xgsj")
    private Date yhXgsj;

    /**
     * 成员属性
     */
    @SerializedName("cysx")
    private Byte cysx;

    /**
     * 人员类型代码(1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员)
     */
    @SerializedName("rylx_dm")
    private Byte rylxDm;

    /**
     * 认证级别(0=未认证，1=强制认证，2=短信认证，16=刷脸认证)
     */
    @SerializedName("rzjb")
    private Byte rzjb;

    /**
     * 认证时间
     */
    @SerializedName("rzsj")
    private Date rzsj;

    /**
     * 同步状态(0=未同步 1=已同步)
     */
    @SerializedName("tbzt")
    private Byte tbzt;

    /**
     * 所属税务机关代码
     */
    @SerializedName("ssswjg_dm")
    private String ssswjgDm;

    /**
     * 删除标记
     */
    @SerializedName("is_delete")
    private Boolean isDelete;

    @SerializedName("cjsj")
    private Date cjsj;

    @SerializedName("xgsj")
    private Date xgsj;

    /**
     * 登记注册类型代码
     */
    @SerializedName("djzclx_dm")
    private String djzclxDm;

    /**
     * 行业代码
     */
    @SerializedName("hy_dm")
    private String hyDm;

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     */
    @SerializedName("zzsnslx")
    private Byte zzsnslx;

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     */
    @SerializedName("xxwlqy_bj")
    private Byte xxwlqyBj;

    /**
     * 激活标记
     */
    @SerializedName("jhbj")
    private Byte jhbj;

    /**
     * 首要联系人标记:0=否,1=是
     */
    @SerializedName("sylxrbj")
    private Byte sylxrbj;
}
