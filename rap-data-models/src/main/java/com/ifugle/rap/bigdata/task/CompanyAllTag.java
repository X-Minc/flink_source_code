package com.ifugle.rap.bigdata.task;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月23日 13:56
 */
@Data
public class CompanyAllTag implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("xnzz_id")
    private Long xnzzId;

    @SerializedName("bm_id")
    private Long bmId;

    @SerializedName("nsr_id")
    private Long nsrId;

    /**
     * 部门集合 所有所属上级部门和本级部门集合
     */
    @SerializedName("bm_ids")
    private List<Long> bmIds;

    /**
     * 纳税人识别号
     */
    @SerializedName("nsrsbh")
    private String nsrsbh;

    /**
     * 统一社会信用代码
     */
    @SerializedName("shxydm")
    private String shxydm;

    @SerializedName("nsrmc")
    private String nsrmc;

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     */
    @SerializedName("xxwlqy_tag")
    private Byte xxwlqyTag;

    /**
     * 登记注册类型代码
     */
    @SerializedName("djzclx_tag")
    private String djzclxTag;

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     */
    @SerializedName("zzsnslx_tag")
    private Byte zzsnslxTag;

    /**
     * 大中小微企业标记(0=未知,1=大型企业,2=中型企业,4=小型企业,8=微型企业)
     */
    @SerializedName("dzxwqy_tag")
    private Byte dzxwqyTag;

    /**
     * 所属税务机关代码
     */
    @SerializedName("ssswjg_tag")
    private String ssswjgDm;

    /**
     * 激活标签(0=删除,1=增加)
     */
    @SerializedName("jh_tag")
    private Byte jhTag;

    /**
     * 认证标签(0=删除,1=增加)
     */
    @SerializedName("rz_tag")
    private Byte rzTag;

    /**
     * 活跃标签(0=删除,1=增加)
     */
    @SerializedName("hy_tag")
    private Byte hyTag;

    /**
     * 周活跃标签(0=删除,1=增加)
     */
    @SerializedName("hyweek_tag")
    private Byte weekHyTag;

    /**
     * 近30天活跃标签(0=删除,1=增加)
     */
    @SerializedName("hy30_tag")
    private Byte thirtyDaysHyTag;

    /**
     * 月活跃标签(0=删除,1=增加)
     */
    @SerializedName("hymm_tag")
    private Byte monthHyTag;

    /**
     * 钉钉活跃标签(0=删除,1=增加)
     */
    @SerializedName("dd_hy_tag")
    private Byte ddHyTag;

    /**
     * 钉钉周活跃标签(0=删除,1=增加)
     */
    @SerializedName("dd_hyweek_tag")
    private Byte ddWeekHyTag;

    /**
     * 钉钉近30天活跃标签(0=删除,1=增加)
     */
    @SerializedName("dd_hy30_tag")
    private Byte ddThirtyDaysHyTag;

    /**
     * 钉钉月活跃标签(0=删除,1=增加)
     */
    @SerializedName("dd_hymm_tag")
    private Byte ddMonthHyTag;

    /**
     * 在册标签(0=删除,1=增加)
     */
    @SerializedName("zc_tag")
    private Byte zcTag;

    /**
     * 活跃日期(yyyy-MM-dd)
     */
    @SerializedName("hyrq")
    private String hyrq;

    /**
     * 第一次活跃日期(yyyy-MM-dd)
     */
    @SerializedName("first_hyrq")
    private String firstHyrq;

    /**
     * 钉钉活跃日期(yyyy-MM-dd)
     */
    @SerializedName("dd_hyrq")
    private String ddHyrq;

    /**
     * 更新日期(yyyy-MM-dd)
     */
    @SerializedName("gxrq")
    private String gxrq;

    /**
     * 企业激活日期 yyyy-MM-dd
     */
    @SerializedName("jhrq")
    private String jhrq;

    /**
     * 变更标签(1=修改，2=删除)
     */
    @SerializedName("bg_tag")
    private Byte bgTag;

    /**
     * 纳税人状态代码
     */
    @SerializedName("nsrzt_dm")
    private Byte nsrztDm;

    /**
     * 删除标记
     */
    @SerializedName("is_delete")
    private Boolean isDelete;

    /**
     * 税务未办理户(1=是,0=否)
     */
    @SerializedName("swwbl_tag")
    private Byte swwblTag;
}
