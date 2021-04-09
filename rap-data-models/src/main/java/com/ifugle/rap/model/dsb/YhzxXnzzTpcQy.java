/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.model.dsb;

import com.ifugle.rap.core.annotation.Label;
import com.ifugle.rap.core.model.impl.EnhanceModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @since 2020-05-22 14:08:18
 * @version $Id$
 * @author ifugle
 */
public class YhzxXnzzTpcQy extends EnhanceModel<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * 虚拟组织IDPartition(hash())
     */
    @NotNull
    @Label("虚拟组织IDPartition")
    private Long xnzzId;

    /**
     * 纳税人id(YHZX_XNZZ_NSR.ID)
     */
    @Label("纳税人id")
    private Long nsrId;

    /**
     * 企业创建的部门ID
     */
    @Label("企业创建的部门ID")
    private Long bmId;

    /**
     * 企业对应钉钉的部门ID
     */
    @Size(max = 50)
    @Label("企业对应钉钉的部门ID")
    private String deptId;

    /**
     * 登记序号(源自金三登记表)
     */
    @Size(max = 50)
    @Label("登记序号")
    private String djxh;

    /**
     * 纳税人识别号#CryptBase36#
     */
    @NotNull
    @Size(max = 32)
    @Label("纳税人识别号")
    private String nsrsbh;

    /**
     * 社会信用代码#CryptBase36#
     */
    @NotNull
    @Size(max = 20)
    @Label("社会信用代码")
    private String shxydm;

    /**
     * 证照号码#CryptBase36#
     */
    @Size(max = 30)
    @Label("证照号码")
    private String zzhm;

    /**
     * 纳税人名称#CryptSimple#
     */
    @NotNull
    @Size(max = 300)
    @Label("纳税人名称")
    private String nsrmc;

    /**
     * 机构部门代码
     */
    @Size(max = 11)
    @Label("机构部门代码")
    private String jgbmDm;

    /**
     * 税管员机构人员代码
     */
    @Size(max = 11)
    @Label("税管员机构人员代码")
    private String sgyJgryDm;

    /**
     * 主管税务所（科、分局）代码
     */
    @Size(max = 11)
    @Label("主管税务所（科、分局）代码")
    private String zgswskfjDm;

    /**
     * 纳税人状态代码
     */
    @NotNull
    @Size(max = 2)
    @Label("纳税人状态代码")
    private String nsrztDm;

    /**
     * 登记注册类型代码
     */
    @Size(max = 3)
    @Label("登记注册类型代码")
    private String djzclxDm;

    /**
     * 行业代码
     */
    @Size(max = 4)
    @Label("行业代码")
    private String hyDm;

    /**
     * 课征主体登记类型代码
     */
    @Size(max = 4)
    @Label("课征主体登记类型代码")
    private String kzztdjlxDm;

    /**
     * 单位隶属关系代码
     */
    @Size(max = 2)
    @Label("单位隶属关系代码")
    private String dwlsgxDm;

    /**
     * 会计制度（准则）代码
     */
    @Size(max = 3)
    @Label("会计制度（准则）代码")
    private String kjzdzzDm;

    /**
     * 注册地址行政区划数字代码
     */
    @Size(max = 6)
    @Label("注册地址行政区划数字代码")
    private String xzqhszDm;

    /**
     * 街道乡镇名称#CryptSimple#
     */
    @Size(max = 300)
    @Label("街道乡镇名称")
    private String jdxzmc;

    /**
     * 经营范围
     */
    @Size(max = 3600)
    @Label("经营范围")
    private String jyfw;

    /**
     * 注册地址#CryptSimple#
     */
    @Size(max = 300)
    @Label("注册地址")
    private String zcdz;

    /**
     * 生产经营地址#CryptSimple#
     */
    @Size(max = 300)
    @Label("生产经营地址")
    private String scjydz;

    /**
     * 开业设立日期
     */
    @Label("开业设立日期")
    private Date kyslrq;

    /**
     * 从业人数
     */
    @Label("从业人数")
    private Integer cyrs;

    /**
     * 重点税源户监控级次代码
     */
    @Size(max = 2)
    @Label("重点税源户监控级次代码")
    private String zdsyhjkjcDm;

    /**
     * 一般纳税人认定标记
     */
    @Size(max = 1)
    @Label("一般纳税人认定标记")
    private String ybnsrrdbj;

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     */
    @Label("增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人")
    private Byte zzsnslx;

    /**
     * 大中小微企业标记(0=未知,1=大型企业,2=中型企业,4=小型企业,8=微型企业)
     */
    @Label("大中小微企业标记")
    private Byte dzxwqyBj;

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     */
    @Label("小型微利企业标记")
    private Byte xxwlqyBj;

    /**
     * 纳税信用等级(0=未知,1=A级,2=B级,4=C级,8=D级,16=M级)
     */
    @Label("纳税信用等级")
    private Byte nsxydj;

    /**
     * 状态:从右往左的标记位依此为(规模以上标志、高新技术标志、新旧动能转换标志)
     */
    @Label("状态:从右往左的标记位依此为(规模以上标志、高新技术标志、新旧动能转换标志)")
    private Integer status;

    /**
     * 删除标记(默认0=否，1=是)
     */
    @Label("删除标记")
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @Size(max = 50)
    @Label("创建人")
    private String cjr;

    /**
     * 创建时间
     */
    @Label("创建时间")
    private Timestamp cjsj;

    /**
     * 修改人
     */
    @Size(max = 50)
    @Label("修改人")
    private String xgr;

    /**
     * 修改时间
     */
    @Label("修改时间")
    private Timestamp xgsj;

    /**
     * 操作标记(1=新增，2=修改，3=删除)
     */
    @Label("操作标记")
    private Byte operate;

    /**
     * 最后一次同步时间
     */
    @Label("最后一次同步时间")
    private Date syncDate;

    /**
     * 最后一次同步信息(成功失败都有)
     */
    @Size(max = 2000)
    @Label("最后一次同步信息")
    private String syncMessage;

    /**
     * 同步次数
     */
    @Label("同步次数")
    private Integer syncTimes;

    /**
     * 最后一次同步错误码0=成功
     */
    @Label("最后一次同步错误码0=成功")
    private Integer syncCode;

    /**
     * 增值税企业类型代码
     */
    @Size(max = 1)
    @Label("增值税企业类型代码")
    private String zzsqylxDm;

    /**
     * 营改增纳税人类型代码
     */
    @Size(max = 2)
    @Label("营改增纳税人类型代码")
    private String ygznsrlxDm;

    /**
     * 登记日期
     */
    @Label("登记日期")
    private Date djrq;

    /**
     * 跨区财产税主体登记(1=是，0=否)
     */
    @Label("跨区财产税主体登记")
    private Boolean kqccsztdj;

    /**
     * 核算方式代码
     */
    @Size(max = 2)
    @Label("核算方式代码")
    private String hsfsDm;

    /**
     * 企业所得税征收方式代码
     */
    @Size(max = 3)
    @Label("企业所得税征收方式代码")
    private String qysdszsfsDm;

    /**
     * 文化事业建设费缴费信息登记(1=是，0=否)
     */
    @Label("文化事业建设费缴费信息登记")
    private Boolean whsyjsfjfxxdj;

    /**
     * 所在片区
     */
    @Label("所在片区")
    private String szpq;

    /**
     * 虚拟组织IDPartition(hash())
     *
     * @return the xnzzId
     */
    public Long getXnzzId() {
        return xnzzId;
    }

    /**
     * 虚拟组织IDPartition(hash())
     *
     * @param xnzzId
     *            the xnzzId to set
     */
    public void setXnzzId(Long xnzzId) {
        this.xnzzId = xnzzId;
    }

    /**
     * 纳税人id(YHZX_XNZZ_NSR.ID)
     *
     * @return the nsrId
     */
    public Long getNsrId() {
        return nsrId;
    }

    /**
     * 纳税人id(YHZX_XNZZ_NSR.ID)
     *
     * @param nsrId
     *            the nsrId to set
     */
    public void setNsrId(Long nsrId) {
        this.nsrId = nsrId;
    }

    /**
     * 企业创建的部门ID
     *
     * @return the bmId
     */
    public Long getBmId() {
        return bmId;
    }

    /**
     * 企业创建的部门ID
     *
     * @param bmId
     *            the bmId to set
     */
    public void setBmId(Long bmId) {
        this.bmId = bmId;
    }

    /**
     * 企业对应钉钉的部门ID
     *
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 企业对应钉钉的部门ID
     *
     * @param deptId
     *            the deptId to set
     */
    public void setDeptId(String deptId) {
        this.deptId = (deptId == null ? null : deptId.trim());
    }

    /**
     * 登记序号(源自金三登记表)
     *
     * @return the djxh
     */
    public String getDjxh() {
        return djxh;
    }

    /**
     * 登记序号(源自金三登记表)
     *
     * @param djxh
     *            the djxh to set
     */
    public void setDjxh(String djxh) {
        this.djxh = (djxh == null ? null : djxh.trim());
    }

    /**
     * 纳税人识别号#CryptBase36#
     *
     * @return the nsrsbh
     */
    public String getNsrsbh() {
        return nsrsbh;
    }

    /**
     * 纳税人识别号#CryptBase36#
     *
     * @param nsrsbh
     *            the nsrsbh to set
     */
    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = (nsrsbh == null ? null : nsrsbh.trim());
    }

    /**
     * 社会信用代码#CryptBase36#
     *
     * @return the shxydm
     */
    public String getShxydm() {
        return shxydm;
    }

    /**
     * 社会信用代码#CryptBase36#
     *
     * @param shxydm
     *            the shxydm to set
     */
    public void setShxydm(String shxydm) {
        this.shxydm = (shxydm == null ? null : shxydm.trim());
    }

    /**
     * 证照号码#CryptBase36#
     *
     * @return the zzhm
     */
    public String getZzhm() {
        return zzhm;
    }

    /**
     * 证照号码#CryptBase36#
     *
     * @param zzhm
     *            the zzhm to set
     */
    public void setZzhm(String zzhm) {
        this.zzhm = (zzhm == null ? null : zzhm.trim());
    }

    /**
     * 纳税人名称#CryptSimple#
     *
     * @return the nsrmc
     */
    public String getNsrmc() {
        return nsrmc;
    }

    /**
     * 纳税人名称#CryptSimple#
     *
     * @param nsrmc
     *            the nsrmc to set
     */
    public void setNsrmc(String nsrmc) {
        this.nsrmc = (nsrmc == null ? null : nsrmc.trim());
    }

    /**
     * 机构部门代码
     *
     * @return the jgbmDm
     */
    public String getJgbmDm() {
        return jgbmDm;
    }

    /**
     * 机构部门代码
     *
     * @param jgbmDm
     *            the jgbmDm to set
     */
    public void setJgbmDm(String jgbmDm) {
        this.jgbmDm = (jgbmDm == null ? null : jgbmDm.trim());
    }

    /**
     * 税管员机构人员代码
     *
     * @return the sgyJgryDm
     */
    public String getSgyJgryDm() {
        return sgyJgryDm;
    }

    /**
     * 税管员机构人员代码
     *
     * @param sgyJgryDm
     *            the sgyJgryDm to set
     */
    public void setSgyJgryDm(String sgyJgryDm) {
        this.sgyJgryDm = (sgyJgryDm == null ? null : sgyJgryDm.trim());
    }

    /**
     * 主管税务所（科、分局）代码
     *
     * @return the zgswskfjDm
     */
    public String getZgswskfjDm() {
        return zgswskfjDm;
    }

    /**
     * 主管税务所（科、分局）代码
     *
     * @param zgswskfjDm
     *            the zgswskfjDm to set
     */
    public void setZgswskfjDm(String zgswskfjDm) {
        this.zgswskfjDm = (zgswskfjDm == null ? null : zgswskfjDm.trim());
    }

    /**
     * 纳税人状态代码
     *
     * @return the nsrztDm
     */
    public String getNsrztDm() {
        return nsrztDm;
    }

    /**
     * 纳税人状态代码
     *
     * @param nsrztDm
     *            the nsrztDm to set
     */
    public void setNsrztDm(String nsrztDm) {
        this.nsrztDm = (nsrztDm == null ? null : nsrztDm.trim());
    }

    /**
     * 登记注册类型代码
     *
     * @return the djzclxDm
     */
    public String getDjzclxDm() {
        return djzclxDm;
    }

    /**
     * 登记注册类型代码
     *
     * @param djzclxDm
     *            the djzclxDm to set
     */
    public void setDjzclxDm(String djzclxDm) {
        this.djzclxDm = (djzclxDm == null ? null : djzclxDm.trim());
    }

    /**
     * 行业代码
     *
     * @return the hyDm
     */
    public String getHyDm() {
        return hyDm;
    }

    /**
     * 行业代码
     *
     * @param hyDm
     *            the hyDm to set
     */
    public void setHyDm(String hyDm) {
        this.hyDm = (hyDm == null ? null : hyDm.trim());
    }

    /**
     * 课征主体登记类型代码
     *
     * @return the kzztdjlxDm
     */
    public String getKzztdjlxDm() {
        return kzztdjlxDm;
    }

    /**
     * 课征主体登记类型代码
     *
     * @param kzztdjlxDm
     *            the kzztdjlxDm to set
     */
    public void setKzztdjlxDm(String kzztdjlxDm) {
        this.kzztdjlxDm = (kzztdjlxDm == null ? null : kzztdjlxDm.trim());
    }

    /**
     * 单位隶属关系代码
     *
     * @return the dwlsgxDm
     */
    public String getDwlsgxDm() {
        return dwlsgxDm;
    }

    /**
     * 单位隶属关系代码
     *
     * @param dwlsgxDm
     *            the dwlsgxDm to set
     */
    public void setDwlsgxDm(String dwlsgxDm) {
        this.dwlsgxDm = (dwlsgxDm == null ? null : dwlsgxDm.trim());
    }

    /**
     * 会计制度（准则）代码
     *
     * @return the kjzdzzDm
     */
    public String getKjzdzzDm() {
        return kjzdzzDm;
    }

    /**
     * 会计制度（准则）代码
     *
     * @param kjzdzzDm
     *            the kjzdzzDm to set
     */
    public void setKjzdzzDm(String kjzdzzDm) {
        this.kjzdzzDm = (kjzdzzDm == null ? null : kjzdzzDm.trim());
    }

    /**
     * 注册地址行政区划数字代码
     *
     * @return the xzqhszDm
     */
    public String getXzqhszDm() {
        return xzqhszDm;
    }

    /**
     * 注册地址行政区划数字代码
     *
     * @param xzqhszDm
     *            the xzqhszDm to set
     */
    public void setXzqhszDm(String xzqhszDm) {
        this.xzqhszDm = (xzqhszDm == null ? null : xzqhszDm.trim());
    }

    /**
     * 街道乡镇名称#CryptSimple#
     *
     * @return the jdxzmc
     */
    public String getJdxzmc() {
        return jdxzmc;
    }

    /**
     * 街道乡镇名称#CryptSimple#
     *
     * @param jdxzmc
     *            the jdxzmc to set
     */
    public void setJdxzmc(String jdxzmc) {
        this.jdxzmc = (jdxzmc == null ? null : jdxzmc.trim());
    }

    /**
     * 经营范围
     *
     * @return the jyfw
     */
    public String getJyfw() {
        return jyfw;
    }

    /**
     * 经营范围
     *
     * @param jyfw
     *            the jyfw to set
     */
    public void setJyfw(String jyfw) {
        this.jyfw = (jyfw == null ? null : jyfw.trim());
    }

    /**
     * 注册地址#CryptSimple#
     *
     * @return the zcdz
     */
    public String getZcdz() {
        return zcdz;
    }

    /**
     * 注册地址#CryptSimple#
     *
     * @param zcdz
     *            the zcdz to set
     */
    public void setZcdz(String zcdz) {
        this.zcdz = (zcdz == null ? null : zcdz.trim());
    }

    /**
     * 生产经营地址#CryptSimple#
     *
     * @return the scjydz
     */
    public String getScjydz() {
        return scjydz;
    }

    /**
     * 生产经营地址#CryptSimple#
     *
     * @param scjydz
     *            the scjydz to set
     */
    public void setScjydz(String scjydz) {
        this.scjydz = (scjydz == null ? null : scjydz.trim());
    }

    /**
     * 开业设立日期
     *
     * @return the kyslrq
     */
    public Date getKyslrq() {
        return kyslrq;
    }

    /**
     * 开业设立日期
     *
     * @param kyslrq
     *            the kyslrq to set
     */
    public void setKyslrq(Date kyslrq) {
        this.kyslrq = kyslrq;
    }

    /**
     * 从业人数
     *
     * @return the cyrs
     */
    public Integer getCyrs() {
        return cyrs;
    }

    /**
     * 从业人数
     *
     * @param cyrs
     *            the cyrs to set
     */
    public void setCyrs(Integer cyrs) {
        this.cyrs = cyrs;
    }

    /**
     * 重点税源户监控级次代码
     *
     * @return the zdsyhjkjcDm
     */
    public String getZdsyhjkjcDm() {
        return zdsyhjkjcDm;
    }

    /**
     * 重点税源户监控级次代码
     *
     * @param zdsyhjkjcDm
     *            the zdsyhjkjcDm to set
     */
    public void setZdsyhjkjcDm(String zdsyhjkjcDm) {
        this.zdsyhjkjcDm = (zdsyhjkjcDm == null ? null : zdsyhjkjcDm.trim());
    }

    /**
     * 一般纳税人认定标记
     *
     * @return the ybnsrrdbj
     */
    public String getYbnsrrdbj() {
        return ybnsrrdbj;
    }

    /**
     * 一般纳税人认定标记
     *
     * @param ybnsrrdbj
     *            the ybnsrrdbj to set
     */
    public void setYbnsrrdbj(String ybnsrrdbj) {
        this.ybnsrrdbj = (ybnsrrdbj == null ? null : ybnsrrdbj.trim());
    }

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     *
     * @return the zzsnslx
     */
    public Byte getZzsnslx() {
        return zzsnslx;
    }

    /**
     * 增值税纳税类型 0：未知 1：一般纳税人 2：小规模纳税人
     *
     * @param zzsnslx
     *            the zzsnslx to set
     */
    public void setZzsnslx(Byte zzsnslx) {
        this.zzsnslx = zzsnslx;
    }

    /**
     * 大中小微企业标记(0=未知,1=大型企业,2=中型企业,4=小型企业,8=微型企业)
     *
     * @return the dzxwqyBj
     */
    public Byte getDzxwqyBj() {
        return dzxwqyBj;
    }

    /**
     * 大中小微企业标记(0=未知,1=大型企业,2=中型企业,4=小型企业,8=微型企业)
     *
     * @param dzxwqyBj
     *            the dzxwqyBj to set
     */
    public void setDzxwqyBj(Byte dzxwqyBj) {
        this.dzxwqyBj = dzxwqyBj;
    }

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     *
     * @return the xxwlqyBj
     */
    public Byte getXxwlqyBj() {
        return xxwlqyBj;
    }

    /**
     * 小型微利企业标记(0=未知,1=是,2=否)
     *
     * @param xxwlqyBj
     *            the xxwlqyBj to set
     */
    public void setXxwlqyBj(Byte xxwlqyBj) {
        this.xxwlqyBj = xxwlqyBj;
    }

    /**
     * 纳税信用等级(0=未知,1=A级,2=B级,4=C级,8=D级,16=M级)
     *
     * @return the nsxydj
     */
    public Byte getNsxydj() {
        return nsxydj;
    }

    /**
     * 纳税信用等级(0=未知,1=A级,2=B级,4=C级,8=D级,16=M级)
     *
     * @param nsxydj
     *            the nsxydj to set
     */
    public void setNsxydj(Byte nsxydj) {
        this.nsxydj = nsxydj;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    /**
     * 删除标记(默认0=否，1=是)
     *
     * @return the isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 删除标记(默认0=否，1=是)
     *
     * @param isDeleted
     *            the isDeleted to set
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 创建人
     *
     * @return the cjr
     */
    public String getCjr() {
        return cjr;
    }

    /**
     * 创建人
     *
     * @param cjr
     *            the cjr to set
     */
    public void setCjr(String cjr) {
        this.cjr = (cjr == null ? null : cjr.trim());
    }

    /**
     * 创建时间
     *
     * @return the cjsj
     */
    public Timestamp getCjsj() {
        return cjsj;
    }

    /**
     * 创建时间
     *
     * @param cjsj
     *            the cjsj to set
     */
    public void setCjsj(Timestamp cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * 修改人
     *
     * @return the xgr
     */
    public String getXgr() {
        return xgr;
    }

    /**
     * 修改人
     *
     * @param xgr
     *            the xgr to set
     */
    public void setXgr(String xgr) {
        this.xgr = (xgr == null ? null : xgr.trim());
    }

    /**
     * 修改时间
     *
     * @return the xgsj
     */
    public Timestamp getXgsj() {
        return xgsj;
    }

    /**
     * 修改时间
     *
     * @param xgsj
     *            the xgsj to set
     */
    public void setXgsj(Timestamp xgsj) {
        this.xgsj = xgsj;
    }

    /**
     * 操作标记(1=新增，2=修改，3=删除)
     *
     * @return the operate
     */
    public Byte getOperate() {
        return operate;
    }

    /**
     * 操作标记(1=新增，2=修改，3=删除)
     *
     * @param operate
     *            the operate to set
     */
    public void setOperate(Byte operate) {
        this.operate = operate;
    }

    /**
     * 最后一次同步时间
     *
     * @return the syncDate
     */
    public Date getSyncDate() {
        return syncDate;
    }

    /**
     * 最后一次同步时间
     *
     * @param syncDate
     *            the syncDate to set
     */
    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    /**
     * 最后一次同步信息(成功失败都有)
     *
     * @return the syncMessage
     */
    public String getSyncMessage() {
        return syncMessage;
    }

    /**
     * 最后一次同步信息(成功失败都有)
     *
     * @param syncMessage
     *            the syncMessage to set
     */
    public void setSyncMessage(String syncMessage) {
        this.syncMessage = (syncMessage == null ? null : syncMessage.trim());
    }

    /**
     * 同步次数
     *
     * @return the syncTimes
     */
    public Integer getSyncTimes() {
        return syncTimes;
    }

    /**
     * 同步次数
     *
     * @param syncTimes
     *            the syncTimes to set
     */
    public void setSyncTimes(Integer syncTimes) {
        this.syncTimes = syncTimes;
    }

    /**
     * 最后一次同步错误码0=成功
     *
     * @return the syncCode
     */
    public Integer getSyncCode() {
        return syncCode;
    }

    /**
     * 最后一次同步错误码0=成功
     *
     * @param syncCode
     *            the syncCode to set
     */
    public void setSyncCode(Integer syncCode) {
        this.syncCode = syncCode;
    }

    public String getZzsqylxDm() {
        return zzsqylxDm;
    }

    public void setZzsqylxDm(String zzsqylxDm) {
        this.zzsqylxDm = zzsqylxDm;
    }

    public String getYgznsrlxDm() {
        return ygznsrlxDm;
    }

    public void setYgznsrlxDm(String ygznsrlxDm) {
        this.ygznsrlxDm = ygznsrlxDm;
    }

    public Date getDjrq() {
        return djrq;
    }

    public void setDjrq(Date djrq) {
        this.djrq = djrq;
    }

    public Boolean getKqccsztdj() {
        return kqccsztdj;
    }

    public void setKqccsztdj(Boolean kqccsztdj) {
        this.kqccsztdj = kqccsztdj;
    }

    public String getHsfsDm() {
        return hsfsDm;
    }

    public void setHsfsDm(String hsfsDm) {
        this.hsfsDm = hsfsDm;
    }

    public String getQysdszsfsDm() {
        return qysdszsfsDm;
    }

    public void setQysdszsfsDm(String qysdszsfsDm) {
        this.qysdszsfsDm = qysdszsfsDm;
    }

    public Boolean getWhsyjsfjfxxdj() {
        return whsyjsfjfxxdj;
    }

    public void setWhsyjsfjfxxdj(Boolean whsyjsfjfxxdj) {
        this.whsyjsfjfxxdj = whsyjsfjfxxdj;
    }

    public String getSzpq() {
        return szpq;
    }

    public void setSzpq(String szpq) {
        this.szpq = szpq;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
        result = prime * result + ((getNsrId() == null) ? 0 : getNsrId().hashCode());
        result = prime * result + ((getBmId() == null) ? 0 : getBmId().hashCode());
        result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
        result = prime * result + ((getDjxh() == null) ? 0 : getDjxh().hashCode());
        result = prime * result + ((getNsrsbh() == null) ? 0 : getNsrsbh().hashCode());
        result = prime * result + ((getShxydm() == null) ? 0 : getShxydm().hashCode());
        result = prime * result + ((getZzhm() == null) ? 0 : getZzhm().hashCode());
        result = prime * result + ((getNsrmc() == null) ? 0 : getNsrmc().hashCode());
        result = prime * result + ((getJgbmDm() == null) ? 0 : getJgbmDm().hashCode());
        result = prime * result + ((getSgyJgryDm() == null) ? 0 : getSgyJgryDm().hashCode());
        result = prime * result + ((getZgswskfjDm() == null) ? 0 : getZgswskfjDm().hashCode());
        result = prime * result + ((getNsrztDm() == null) ? 0 : getNsrztDm().hashCode());
        result = prime * result + ((getDjzclxDm() == null) ? 0 : getDjzclxDm().hashCode());
        result = prime * result + ((getHyDm() == null) ? 0 : getHyDm().hashCode());
        result = prime * result + ((getKzztdjlxDm() == null) ? 0 : getKzztdjlxDm().hashCode());
        result = prime * result + ((getDwlsgxDm() == null) ? 0 : getDwlsgxDm().hashCode());
        result = prime * result + ((getKjzdzzDm() == null) ? 0 : getKjzdzzDm().hashCode());
        result = prime * result + ((getXzqhszDm() == null) ? 0 : getXzqhszDm().hashCode());
        result = prime * result + ((getJdxzmc() == null) ? 0 : getJdxzmc().hashCode());
        result = prime * result + ((getJyfw() == null) ? 0 : getJyfw().hashCode());
        result = prime * result + ((getZcdz() == null) ? 0 : getZcdz().hashCode());
        result = prime * result + ((getScjydz() == null) ? 0 : getScjydz().hashCode());
        result = prime * result + ((getKyslrq() == null) ? 0 : getKyslrq().hashCode());
        result = prime * result + ((getCyrs() == null) ? 0 : getCyrs().hashCode());
        result = prime * result + ((getZdsyhjkjcDm() == null) ? 0 : getZdsyhjkjcDm().hashCode());
        result = prime * result + ((getYbnsrrdbj() == null) ? 0 : getYbnsrrdbj().hashCode());
        result = prime * result + ((getZzsnslx() == null) ? 0 : getZzsnslx().hashCode());
        result = prime * result + ((getDzxwqyBj() == null) ? 0 : getDzxwqyBj().hashCode());
        result = prime * result + ((getXxwlqyBj() == null) ? 0 : getXxwlqyBj().hashCode());
        result = prime * result + ((getNsxydj() == null) ? 0 : getNsxydj().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getCjr() == null) ? 0 : getCjr().hashCode());
        result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
        result = prime * result + ((getXgr() == null) ? 0 : getXgr().hashCode());
        result = prime * result + ((getXgsj() == null) ? 0 : getXgsj().hashCode());
        result = prime * result + ((getOperate() == null) ? 0 : getOperate().hashCode());
        result = prime * result + ((getSyncDate() == null) ? 0 : getSyncDate().hashCode());
        result = prime * result + ((getSyncMessage() == null) ? 0 : getSyncMessage().hashCode());
        result = prime * result + ((getSyncTimes() == null) ? 0 : getSyncTimes().hashCode());
        result = prime * result + ((getSyncCode() == null) ? 0 : getSyncCode().hashCode());
        result = prime * result + ((getZzsqylxDm() == null) ? 0 : getZzsqylxDm().hashCode());
        result = prime * result + ((getYgznsrlxDm() == null) ? 0 : getYgznsrlxDm().hashCode());
        result = prime * result + ((getDjrq() == null) ? 0 : getDjrq().hashCode());
        result = prime * result + ((getKqccsztdj() == null) ? 0 : getKqccsztdj().hashCode());
        result = prime * result + ((getHsfsDm() == null) ? 0 : getHsfsDm().hashCode());
        result = prime * result + ((getQysdszsfsDm() == null) ? 0 : getQysdszsfsDm().hashCode());
        result = prime * result + ((getWhsyjsfjfxxdj() == null) ? 0 : getWhsyjsfjfxxdj().hashCode());
        result = prime * result + ((getSzpq() == null) ? 0 : getSzpq().hashCode());
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
        YhzxXnzzTpcQy other = (YhzxXnzzTpcQy) obj;
        return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
                && (this.getNsrId() == null ? other.getNsrId() == null : this.getNsrId().equals(other.getNsrId()))
                && (this.getBmId() == null ? other.getBmId() == null : this.getBmId().equals(other.getBmId()))
                && (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
                && (this.getDjxh() == null ? other.getDjxh() == null : this.getDjxh().equals(other.getDjxh()))
                && (this.getNsrsbh() == null ? other.getNsrsbh() == null : this.getNsrsbh().equals(other.getNsrsbh()))
                && (this.getShxydm() == null ? other.getShxydm() == null : this.getShxydm().equals(other.getShxydm()))
                && (this.getZzhm() == null ? other.getZzhm() == null : this.getZzhm().equals(other.getZzhm()))
                && (this.getNsrmc() == null ? other.getNsrmc() == null : this.getNsrmc().equals(other.getNsrmc()))
                && (this.getJgbmDm() == null ? other.getJgbmDm() == null : this.getJgbmDm().equals(other.getJgbmDm()))
                && (this.getSgyJgryDm() == null ? other.getSgyJgryDm() == null : this.getSgyJgryDm().equals(other.getSgyJgryDm()))
                && (this.getZgswskfjDm() == null ? other.getZgswskfjDm() == null : this.getZgswskfjDm().equals(other.getZgswskfjDm()))
                && (this.getNsrztDm() == null ? other.getNsrztDm() == null : this.getNsrztDm().equals(other.getNsrztDm()))
                && (this.getDjzclxDm() == null ? other.getDjzclxDm() == null : this.getDjzclxDm().equals(other.getDjzclxDm()))
                && (this.getHyDm() == null ? other.getHyDm() == null : this.getHyDm().equals(other.getHyDm()))
                && (this.getKzztdjlxDm() == null ? other.getKzztdjlxDm() == null : this.getKzztdjlxDm().equals(other.getKzztdjlxDm()))
                && (this.getDwlsgxDm() == null ? other.getDwlsgxDm() == null : this.getDwlsgxDm().equals(other.getDwlsgxDm()))
                && (this.getKjzdzzDm() == null ? other.getKjzdzzDm() == null : this.getKjzdzzDm().equals(other.getKjzdzzDm()))
                && (this.getXzqhszDm() == null ? other.getXzqhszDm() == null : this.getXzqhszDm().equals(other.getXzqhszDm()))
                && (this.getJdxzmc() == null ? other.getJdxzmc() == null : this.getJdxzmc().equals(other.getJdxzmc()))
                && (this.getJyfw() == null ? other.getJyfw() == null : this.getJyfw().equals(other.getJyfw()))
                && (this.getZcdz() == null ? other.getZcdz() == null : this.getZcdz().equals(other.getZcdz()))
                && (this.getScjydz() == null ? other.getScjydz() == null : this.getScjydz().equals(other.getScjydz()))
                && (this.getKyslrq() == null ? other.getKyslrq() == null : this.getKyslrq().equals(other.getKyslrq()))
                && (this.getCyrs() == null ? other.getCyrs() == null : this.getCyrs().equals(other.getCyrs()))
                && (this.getZdsyhjkjcDm() == null ? other.getZdsyhjkjcDm() == null : this.getZdsyhjkjcDm().equals(other.getZdsyhjkjcDm()))
                && (this.getYbnsrrdbj() == null ? other.getYbnsrrdbj() == null : this.getYbnsrrdbj().equals(other.getYbnsrrdbj()))
                && (this.getZzsnslx() == null ? other.getZzsnslx() == null : this.getZzsnslx().equals(other.getZzsnslx()))
                && (this.getDzxwqyBj() == null ? other.getDzxwqyBj() == null : this.getDzxwqyBj().equals(other.getDzxwqyBj()))
                && (this.getXxwlqyBj() == null ? other.getXxwlqyBj() == null : this.getXxwlqyBj().equals(other.getXxwlqyBj()))
                && (this.getNsxydj() == null ? other.getNsxydj() == null : this.getNsxydj().equals(other.getNsxydj()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
                && (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
                && (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
                && (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()))
                && (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()))
                && (this.getOperate() == null ? other.getOperate() == null : this.getOperate().equals(other.getOperate()))
                && (this.getSyncDate() == null ? other.getSyncDate() == null : this.getSyncDate().equals(other.getSyncDate()))
                && (this.getSyncMessage() == null ? other.getSyncMessage() == null : this.getSyncMessage().equals(other.getSyncMessage()))
                && (this.getSyncTimes() == null ? other.getSyncTimes() == null : this.getSyncTimes().equals(other.getSyncTimes()))
                && (this.getSyncCode() == null ? other.getSyncCode() == null : this.getSyncCode().equals(other.getSyncCode()))
                && (this.getZzsqylxDm() == null ? other.getZzsqylxDm() == null : this.getZzsqylxDm().equals(other.getZzsqylxDm()))
                && (this.getYgznsrlxDm() == null ? other.getYgznsrlxDm() == null : this.getYgznsrlxDm().equals(other.getYgznsrlxDm()))
                && (this.getDjrq() == null ? other.getDjrq() == null : this.getDjrq().equals(other.getDjrq()))
                && (this.getKqccsztdj() == null ? other.getKqccsztdj() == null : this.getKqccsztdj().equals(other.getKqccsztdj()))
                && (this.getHsfsDm() == null ? other.getHsfsDm() == null : this.getHsfsDm().equals(other.getHsfsDm()))
                && (this.getQysdszsfsDm() == null ? other.getQysdszsfsDm() == null : this.getQysdszsfsDm().equals(other.getQysdszsfsDm()))
                && (this.getWhsyjsfjfxxdj() == null ? other.getWhsyjsfjfxxdj() == null : this.getWhsyjsfjfxxdj().equals(other.getWhsyjsfjfxxdj()))
                && (this.getSzpq() == null ? other.getSzpq() == null : this.getSzpq().equals(other.getSzpq()));
    }

}