package com.ifugle.rap.bigdata.task;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2019年08月05日 10:08
 */
@Data
public class DepartAggDw implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 虚拟组织ID
     */
    @SerializedName("xnzz_id")
    private Long xnzzId;

    /**
     * 部门ID
     */
    @SerializedName("bm_id")
    private Long bmId;

    /**
     * 部门名称
     */
    @SerializedName("bmmc")
    private String bmmc;

    /**
     * 上级部门ID
     */
    @SerializedName("p_bm_id")
    private Long pBmId;

    /**
     * 是否是叶子节点部门
     */
    @SerializedName("is_leaf_bm")
    private Boolean isLeafBm;

    /**
     * 部门属性
     */
    @SerializedName("bmsx")
    private Byte bmsx;

    /**
     * 在父部门次序值
     */
    @SerializedName("xssx")
    private Long xssx;

    /**
     * 企业总数
     */
    @SerializedName("qys")
    private Integer qys;

    /**
     * 企业激活数
     */
    @SerializedName("qyjhs")
    private Integer qyjhs;

    /**
     * 机构人员总数
     */
    @SerializedName("jgryzs")
    private Integer jgryzs;

    /**
     * 机构人员激活数
     */
    @SerializedName("jgryjhzs")
    private Integer jgryjhzs;

    /**
     * 机构人员日活跃总数
     */
    @SerializedName("jgryhyzs")
    private Integer jgryhyzs;

    /**
     * 机构人员月活跃总数
     */
    @SerializedName("jgryhyzs_month")
    private Integer jgryhyzsMonth;

    /**
     * 机构人员周活跃总数
     */
    @SerializedName("jgryhyzs_week")
    private Integer jgryhyzsWeek;

    /**
     * 企业人员总数
     */
    @SerializedName("qyryzs")
    private Integer qyryzs;

    /**
     * 企业人员激活总数
     */
    @SerializedName("qyryjhzs")
    private Integer qyryjhzs;

    /**
     * 企业人员日活跃总数
     */
    @SerializedName("qyryhyzs")
    private Integer qyryhyzs;

    /**
     * 企业人员月活跃总数
     */
    @SerializedName("qyryhyzs_month")
    private Integer qyryhyzsMonth;

    /**
     * 企业人员周活跃总数
     */
    @SerializedName("qyryhyzs_week")
    private Integer qyryhyzsWeek;

    /**
     * 人员总数
     */
    @SerializedName("ryzs")
    private Integer ryzs;

    /**
     * 人员激活数
     */
    @SerializedName("ryjhzs")
    private Integer ryjhzs;


    /**
     * 自然人员总数
     */
    @SerializedName("zrryzs")
    private Integer zrryzs;

    /**
     * 自然人员激活数
     */
    @SerializedName("zrryjhzs")
    private Integer zrryjhzs;

    /**
     * 法定代表人总数
     */
    @SerializedName("fddbrzs")
    private Integer fddbrzs;

    /**
     * 法定代表人激活总数
     */
    @SerializedName("fddbrjhzs")
    private Integer fddbrjhzs;

    /**
     * 财务负责人总数
     */
    @SerializedName("cwfzrzs")
    private Integer cwfzrzs;

    /**
     * 财务负责人激活总数
     */
    @SerializedName("cwfzrjhzs")
    private Integer cwfzrjhzs;

    /**
     * 办税人总数
     */
    @SerializedName("bsrzs")
    private Integer bsrzs;

    /**
     * 办税人激活总数
     */
    @SerializedName("bsrjhzs")
    private Integer bsrjhzs;

    /**
     * 其他办税人总数
     */
    @SerializedName("qtbsrzs")
    private Integer qtbsrzs;

    /**
     * 其他办税人激活总数
     */
    @SerializedName("qtbsrjhzs")
    private Integer qtbsrjhzs;

    /**
     * 购票员总数
     */
    @SerializedName("gpyzs")
    private Integer gpyzs;

    /**
     * 购票员激活总数
     */
    @SerializedName("gpyjhzs")
    private Integer gpyjhzs;

    /**
     * 删除标记
     */
    @SerializedName("is_delete")
    private Boolean isDelete;

    /**
     * 丁税宝用户人员总数
     */
    @SerializedName("dsbryzs")
    private Integer dsbryzs;

    /**
     * 丁税宝用户人员激活数
     */
    @SerializedName("dsbryjhzs")
    private Integer dsbryjhzs;

    /**
     * 部门集合 所有所属上级部门和本级部门集合
     */
    @SerializedName("bm_ids")
    private List<Long> bmIds;

    /**
     * 企业注销数
     */
    @SerializedName("qyzxs")
    private Integer qyzxs;

    /**
     * 企业税务未办理户数
     */
    @SerializedName("qywbls")
    private Integer qywbls;

    /**
     * 企业非个体总数
     */
    @SerializedName("qyfgts")
    private Integer qyfgts;

    /**
     * 企业非个体激活数
     */
    @SerializedName("qyfgtjhs")
    private Integer qyfgtjhs;

    /**
     * 企业个体总数
     */
    @SerializedName("qygts")
    private Integer qygts;

    /**
     * 企业个体激活数
     */
    @SerializedName("qygtjhs")
    private Integer qygtjhs;

    /**
     * 初始化统计数，默认为0
     */
    public void initialize() {
        qys = 0;
        qyjhs = 0;
        ryzs = 0;
        ryjhzs = 0;
        zrryzs = 0;
        zrryjhzs = 0;
        jgryzs = 0;
        jgryjhzs = 0;
        qyryzs = 0;
        qyryjhzs = 0;
        fddbrzs = 0;
        fddbrjhzs = 0;
        cwfzrzs = 0;
        cwfzrjhzs = 0;
        bsrzs = 0;
        bsrjhzs = 0;
        qtbsrzs = 0;
        qtbsrjhzs = 0;
        dsbryzs = 0;
        dsbryjhzs = 0;
        jgryhyzs = 0;
        qyryhyzs = 0;
        jgryhyzsMonth = 0;
        qyryhyzsMonth = 0;
        jgryhyzsWeek = 0;
        qyryhyzsWeek = 0;
        gpyzs = 0;
        gpyjhzs = 0;
        qyzxs = 0;
        qywbls = 0;
        qyfgts = 0;
        qyfgtjhs = 0;
        qygts = 0;
        qygtjhs = 0;
    }
}
