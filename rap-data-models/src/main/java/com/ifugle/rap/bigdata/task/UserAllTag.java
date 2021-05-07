package com.ifugle.rap.bigdata.task;

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
public class UserAllTag implements Serializable {
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
     * 成员属性
     */
    @SerializedName("cysx_tag")
    private Byte cysxTag;

    /**
     * 人员类型代码(1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员)
     */
    @SerializedName("rylx_tag")
    private Byte rylxTag;

    /**
     * 认证级别(0=未认证，1=强制认证，2=短信认证，16=刷脸认证)
     */
    @SerializedName("rzjb_tag")
    private Byte rzjbTag;

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
     * 同步标签(0=删除,1=增加)
     */
    @SerializedName("tb_tag")
    private Byte tbTag;

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
     * 当天最早一次，活跃访问来源(0:工作台入口,1:服务号入口,2:邀请短信入口,3:消息推送模板入口,4:钉钉推送消息入口)
     */
    @SerializedName("hy_source_tag")
    private Byte hySourceTag;

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
     * 变更标签(1=修改，2=删除)
     */
    @SerializedName("bg_tag")
    private Byte bgTag;

    /**
     * 用户激活时间 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("jhsj")
    private Date yhJhsj;

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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("yh_cjsj")
    private Date yhCjsj;

    /**
     * 活跃类型标签：
     * 1：当日导入-激活-0活跃人数：当日导入-当日激活-但未登入应用的人数
     * 2：当日导入-激活-活跃人数：当日导入-当日激活-且当日登入应用的人数
     * 3：历史导入-历史激活-活跃人数：历史导入且激活，但无活跃但用户在当日登入应用的人数
     * 4：历史导入-当日激活-活跃人数：历史导入，且从未激活的用户，在当日激活并登入应用的人数
     */
    @SerializedName("hy_type_tag")
    private Byte hyTypeTag;

    /**
     * 用户留存活跃标签：
     * 1：流失用户：已激活，且近90天内无活跃
     * 2：回流用户：流失用户，且近7天内有活跃
     * 3：沉默用户：已激活，且近90天内有活跃且近60天内无活跃
     * 4：低频活跃用户：已激活，且近60天内活跃天数活跃天数小于等于5天
     * 5：一般活跃用户：已激活，且近60天内活跃天数在6-15天
     * 6：高频活跃用户：已激活，且近60天内活跃天数大于15天
     */
    @SerializedName("yhlc_hy_tag")
    private Byte yhlcHyTag;

    /**
     * 回流用户时间 yyyy-MM-dd
     * 回流用户：流失用户，且近7天内有活跃（近7天内的第一天活跃时间）
     */
    @SerializedName("last_hlrq")
    private String lastHlrq;

    /**
     * 人员状态
     */
    @SerializedName("cyzt_tag")
    private Byte cyztTag;

    /**
     * 首要联系人标记:0=否,1=是
     */
    @SerializedName("sylxrbj")
    private Byte sylxrbj;
}
