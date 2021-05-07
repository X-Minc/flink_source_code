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
public class CompanyOds implements Serializable {
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

    @SerializedName("zdsyhbj")
    private Byte zdsyhbj;

    /**
     * 激活标记
     */
    @SerializedName("jhbj")
    private Byte jhbj;

    @SerializedName("yxcys")
    private Integer yxcys;

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
     * 纳税人状态代码
     */
    @SerializedName("nsrzt_dm")
    private String nsrztDm;

    @SerializedName("zzzcbj")
    private Byte zzzcbj;

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     */
    @SerializedName("zzsnslx")
    private Byte zzsnslx;

    /**
     * 大中小微企业标记(0=未知,1=大型企业,2=中型企业,4=小型企业,8=微型企业)
     */
    @SerializedName("dzxwqy_bj")
    private Byte dzxwqyBj;

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     */
    @SerializedName("xxwlqy_bj")
    private Byte xxwlqyBj;

    /**
     * 纳税信用等级(0=未知,1=A级,2=B级,4=C级,8=D级,16=M级)
     */
    @SerializedName("nsxydj")
    private Byte nsxydj;

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
     * 登记序号
     */
    @SerializedName("djxh")
    private String djxh;

    /**
     * 主管国税机关代码
     */
    @SerializedName("zggsjg_dm")
    private String zggsjgDm;

    /**
     * 纳税人简称
     */
    @SerializedName("nsrjc")
    private String nsrjc;

    /**
     * 上级社会信用代码
     */
    @SerializedName("sjshxydm")
    private String sjshxydm;

    /**
     * 开户银行
     */
    @SerializedName("khyh")
    private String khyh;

    /**
     * 银行账号
     */
    @SerializedName("yhzh")
    private String yhzh;

    /**
     * 注册地址
     */
    @SerializedName("zcdz")
    private String zcdz;

    /**
     * 注册地联系电话
     */
    @SerializedName("zcdlxdh")
    private String zcdlxdh;

    /**
     * 生产经营地址
     */
    @SerializedName("scjydz")
    private String scjydz;

    /**
     * 生产经营地联系电话
     */
    @SerializedName("scjydlxdh")
    private String scjydlxdh;

    /**
     * 课征主体登记类型代码
     */
    @SerializedName("kzztdjlx_dm")
    private String kzztdjlxDm;

    /**
     * 经营范围
     */
    @SerializedName("jyfw")
    private String jyfw;

    /**
     * 创建人
     */
    @SerializedName("cjr")
    private String cjr;

    /**
     * 修改人
     */
    @SerializedName("xgr")
    private String xgr;

    /**
     * 标签ID
     */
    @SerializedName("bq_ids")
    private List<Long> bqIds;

    /**
     * 税务未办理户(1=是,0=否)
     */
    @SerializedName("swwblh")
    private Byte swwblh;
}
