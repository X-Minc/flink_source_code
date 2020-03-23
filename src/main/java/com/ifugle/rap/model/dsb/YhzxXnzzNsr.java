/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.model.dsb;

import com.ifugle.rap.core.annotation.Label;
import com.ifugle.rap.core.model.impl.EnhanceModel;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @since 2020-03-22 16:06:06
 * @version $Id$
 * @author ifugle
 */
public class YhzxXnzzNsr extends EnhanceModel<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * 虚拟组织ID
     */
    @NotNull
    @Label("虚拟组织ID")
    private Long xnzzId;

    /**
     * 部门ID
     */
    @NotNull
    @Label("部门ID")
    private Long bmId;

    /**
     * 登记序号
     */
    @Size(max = 50)
    @Label("登记序号")
    private String djxh;

    /**
     * 纳税人识别号#CryptBase36#
     */
    @Size(max = 32)
    @Label("纳税人识别号")
    private String nsrsbh;

    /**
     * 社会信用代码#CryptBase36#
     */
    @Size(max = 20)
    @Label("社会信用代码")
    private String shxydm;

    /**
     * 社会信用代码6(后6位)#CryptBase36#
     */
    @Size(max = 6)
    @Label("社会信用代码6")
    private String shxydm6;

    /**
     * 纳税人名称#CryptSimple#
     */
    @Size(max = 300)
    @Label("纳税人名称")
    private String nsrmc;

    /**
     * 纳税人简称#CryptSimple#
     */
    @Size(max = 300)
    @Label("纳税人简称")
    private String nsrjc;

    /**
     * 上级社会信用代码#CryptBase36#
     */
    @Size(max = 20)
    @Label("上级社会信用代码")
    private String sjshxydm;

    /**
     * 纳税人状态代码
     */
    @Size(max = 4)
    @Label("纳税人状态代码")
    private String nsrztDm;

    /**
     * 激活标记
     */
    @NotNull
    @Label("激活标记")
    private Byte jhbj;

    /**
     * 重点税源户标记(0=否，1=是)
     */
    @Label("重点税源户标记")
    private Byte zdsyhbj;

    /**
     * 有效成员数（短信状态正常成员数）
     */
    @Label("有效成员数（短信状态正常成员数）")
    private Integer yxcys;

    /**
     * 主管国税机关代码
     */
    @Size(max = 11)
    @Label("主管国税机关代码")
    private String zggsjgDm;

    /**
     * 主管地税机关代码
     */
    @Size(max = 11)
    @Label("主管地税机关代码")
    private String zgdsjgDm;

    /**
     * 登记注册类型代码
     */
    @Size(max = 3)
    @Label("登记注册类型代码")
    private String djzclxDm;

    /**
     * 父级登记注册类型代码
     */
    @Size(max = 3)
    @Label("父级登记注册类型代码")
    private String parentDjzclxDm;

    /**
     * 行业代码
     */
    @Size(max = 4)
    @Label("行业代码")
    private String hyDm;

    /**
     * 自助注册标记 ，为null表示已经开通，1表示关闭
     */
    @Label("自助注册标记 ，为null表示已经开通，1表示关闭")
    private Byte zzzcbj;

    /**
     * 开户银行#CryptSimple#
     */
    @Size(max = 300)
    @Label("开户银行")
    private String khyh;

    /**
     * 银行账号#CryptNumber#
     */
    @Size(max = 50)
    @Label("银行账号")
    private String yhzh;

    /**
     * 注册地址#CryptSimple#
     */
    @Size(max = 300)
    @Label("注册地址")
    private String zcdz;

    /**
     * 注册地联系电话#CryptNumber#
     */
    @Size(max = 56)
    @Label("注册地联系电话")
    private String zcdlxdh;

    /**
     * 生产经营地址#CryptSimple#
     */
    @Size(max = 300)
    @Label("生产经营地址")
    private String scjydz;

    /**
     * 生产经营地联系电话#CryptNumber#
     */
    @Size(max = 56)
    @Label("生产经营地联系电话")
    private String scjydlxdh;

    /**
     * 企业所得税征收标记（0=未知，1=国税，2=地税）
     */
    @Label("企业所得税征收标记（0=未知，1=国税，2=地税）")
    private Byte qysdszsbj;

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
     * 课征主体登记类型代码
     */
    @Size(max = 4)
    @Label("课征主体登记类型代码")
    private String kzztdjlxDm;

    /**
     * 经营范围
     */
    @Size(max = 3600)
    @Label("经营范围")
    private String jyfw;

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
     * 会计制度（准则）代码
     */
    @Size(max = 3)
    @Label("会计制度（准则）代码")
    private String kjzdzzDm;

    /**
     * 文化事业建设费缴费信息登记(1=是，0=否)
     */
    @Label("文化事业建设费缴费信息登记")
    private Boolean whsyjsfjfxxdj;

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
     * 主税种征收项目代码
     */
    @Size(max = 5)
    @Label("主税种征收项目代码")
    private String zszZsxmDm;

    /**
     * 企业所得税征收方式代码
     */
    @Size(max = 3)
    @Label("企业所得税征收方式代码")
    private String qysdszsfsDm;

    /**
     * 纳税人corpid(废弃)
     */
    @Size(max = 50)
    @Label("纳税人corpid")
    private String corpId;

    /**
     * 删除标记
     */
    @Label("删除标记")
    private Boolean isDelete;

    /**
     * 创建人
     */
    @NotNull
    @Size(max = 50)
    @Label("创建人")
    private String cjr;

    /**
     * 创建时间
     */
    @Label("创建时间")
    private Date cjsj;

    /**
     * 修改人
     */
    @NotNull
    @Size(max = 50)
    @Label("修改人")
    private String xgr;

    /**
     * 修改时间
     */
    @Label("修改时间")
    private Date xgsj;

    /**
     * 虚拟组织ID
     *
     * @return the xnzzId
     */
    public Long getXnzzId() {
        return xnzzId;
    }

    /**
     * 虚拟组织ID
     *
     * @param xnzzId
     *            the xnzzId to set
     */
    public void setXnzzId(Long xnzzId) {
        this.xnzzId = xnzzId;
    }

    /**
     * 部门ID
     *
     * @return the bmId
     */
    public Long getBmId() {
        return bmId;
    }

    /**
     * 部门ID
     *
     * @param bmId
     *            the bmId to set
     */
    public void setBmId(Long bmId) {
        this.bmId = bmId;
    }

    /**
     * 登记序号
     *
     * @return the djxh
     */
    public String getDjxh() {
        return djxh;
    }

    /**
     * 登记序号
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
     * 社会信用代码6(后6位)#CryptBase36#
     *
     * @return the shxydm6
     */
    public String getShxydm6() {
        return shxydm6;
    }

    /**
     * 社会信用代码6(后6位)#CryptBase36#
     *
     * @param shxydm6
     *            the shxydm6 to set
     */
    public void setShxydm6(String shxydm6) {
        this.shxydm6 = (shxydm6 == null ? null : shxydm6.trim());
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
     * 纳税人简称#CryptSimple#
     *
     * @return the nsrjc
     */
    public String getNsrjc() {
        return nsrjc;
    }

    /**
     * 纳税人简称#CryptSimple#
     *
     * @param nsrjc
     *            the nsrjc to set
     */
    public void setNsrjc(String nsrjc) {
        this.nsrjc = (nsrjc == null ? null : nsrjc.trim());
    }

    /**
     * 上级社会信用代码#CryptBase36#
     *
     * @return the sjshxydm
     */
    public String getSjshxydm() {
        return sjshxydm;
    }

    /**
     * 上级社会信用代码#CryptBase36#
     *
     * @param sjshxydm
     *            the sjshxydm to set
     */
    public void setSjshxydm(String sjshxydm) {
        this.sjshxydm = (sjshxydm == null ? null : sjshxydm.trim());
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
     * 激活标记
     *
     * @return the jhbj
     */
    public Byte getJhbj() {
        return jhbj;
    }

    /**
     * 激活标记
     *
     * @param jhbj
     *            the jhbj to set
     */
    public void setJhbj(Byte jhbj) {
        this.jhbj = jhbj;
    }

    /**
     * 重点税源户标记(0=否，1=是)
     *
     * @return the zdsyhbj
     */
    public Byte getZdsyhbj() {
        return zdsyhbj;
    }

    /**
     * 重点税源户标记(0=否，1=是)
     *
     * @param zdsyhbj
     *            the zdsyhbj to set
     */
    public void setZdsyhbj(Byte zdsyhbj) {
        this.zdsyhbj = zdsyhbj;
    }

    /**
     * 有效成员数（短信状态正常成员数）
     *
     * @return the yxcys
     */
    public Integer getYxcys() {
        return yxcys;
    }

    /**
     * 有效成员数（短信状态正常成员数）
     *
     * @param yxcys
     *            the yxcys to set
     */
    public void setYxcys(Integer yxcys) {
        this.yxcys = yxcys;
    }

    /**
     * 主管国税机关代码
     *
     * @return the zggsjgDm
     */
    public String getZggsjgDm() {
        return zggsjgDm;
    }

    /**
     * 主管国税机关代码
     *
     * @param zggsjgDm
     *            the zggsjgDm to set
     */
    public void setZggsjgDm(String zggsjgDm) {
        this.zggsjgDm = (zggsjgDm == null ? null : zggsjgDm.trim());
    }

    /**
     * 主管地税机关代码
     *
     * @return the zgdsjgDm
     */
    public String getZgdsjgDm() {
        return zgdsjgDm;
    }

    /**
     * 主管地税机关代码
     *
     * @param zgdsjgDm
     *            the zgdsjgDm to set
     */
    public void setZgdsjgDm(String zgdsjgDm) {
        this.zgdsjgDm = (zgdsjgDm == null ? null : zgdsjgDm.trim());
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
     * 父级登记注册类型代码
     *
     * @return the parentDjzclxDm
     */
    public String getParentDjzclxDm() {
        return parentDjzclxDm;
    }

    /**
     * 父级登记注册类型代码
     *
     * @param parentDjzclxDm
     *            the parentDjzclxDm to set
     */
    public void setParentDjzclxDm(String parentDjzclxDm) {
        this.parentDjzclxDm = (parentDjzclxDm == null ? null : parentDjzclxDm.trim());
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
     * 自助注册标记 ，为null表示已经开通，1表示关闭
     *
     * @return the zzzcbj
     */
    public Byte getZzzcbj() {
        return zzzcbj;
    }

    /**
     * 自助注册标记 ，为null表示已经开通，1表示关闭
     *
     * @param zzzcbj
     *            the zzzcbj to set
     */
    public void setZzzcbj(Byte zzzcbj) {
        this.zzzcbj = zzzcbj;
    }

    /**
     * 开户银行#CryptSimple#
     *
     * @return the khyh
     */
    public String getKhyh() {
        return khyh;
    }

    /**
     * 开户银行#CryptSimple#
     *
     * @param khyh
     *            the khyh to set
     */
    public void setKhyh(String khyh) {
        this.khyh = (khyh == null ? null : khyh.trim());
    }

    /**
     * 银行账号#CryptNumber#
     *
     * @return the yhzh
     */
    public String getYhzh() {
        return yhzh;
    }

    /**
     * 银行账号#CryptNumber#
     *
     * @param yhzh
     *            the yhzh to set
     */
    public void setYhzh(String yhzh) {
        this.yhzh = (yhzh == null ? null : yhzh.trim());
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
     * 注册地联系电话#CryptNumber#
     *
     * @return the zcdlxdh
     */
    public String getZcdlxdh() {
        return zcdlxdh;
    }

    /**
     * 注册地联系电话#CryptNumber#
     *
     * @param zcdlxdh
     *            the zcdlxdh to set
     */
    public void setZcdlxdh(String zcdlxdh) {
        this.zcdlxdh = (zcdlxdh == null ? null : zcdlxdh.trim());
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
     * 生产经营地联系电话#CryptNumber#
     *
     * @return the scjydlxdh
     */
    public String getScjydlxdh() {
        return scjydlxdh;
    }

    /**
     * 生产经营地联系电话#CryptNumber#
     *
     * @param scjydlxdh
     *            the scjydlxdh to set
     */
    public void setScjydlxdh(String scjydlxdh) {
        this.scjydlxdh = (scjydlxdh == null ? null : scjydlxdh.trim());
    }

    /**
     * 企业所得税征收标记（0=未知，1=国税，2=地税）
     *
     * @return the qysdszsbj
     */
    public Byte getQysdszsbj() {
        return qysdszsbj;
    }

    /**
     * 企业所得税征收标记（0=未知，1=国税，2=地税）
     *
     * @param qysdszsbj
     *            the qysdszsbj to set
     */
    public void setQysdszsbj(Byte qysdszsbj) {
        this.qysdszsbj = qysdszsbj;
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
     * 登记日期
     *
     * @return the djrq
     */
    public Date getDjrq() {
        return djrq;
    }

    /**
     * 登记日期
     *
     * @param djrq
     *            the djrq to set
     */
    public void setDjrq(Date djrq) {
        this.djrq = djrq;
    }

    /**
     * 跨区财产税主体登记(1=是，0=否)
     *
     * @return the kqccsztdj
     */
    public Boolean getKqccsztdj() {
        return kqccsztdj;
    }

    /**
     * 跨区财产税主体登记(1=是，0=否)
     *
     * @param kqccsztdj
     *            the kqccsztdj to set
     */
    public void setKqccsztdj(Boolean kqccsztdj) {
        this.kqccsztdj = kqccsztdj;
    }

    /**
     * 核算方式代码
     *
     * @return the hsfsDm
     */
    public String getHsfsDm() {
        return hsfsDm;
    }

    /**
     * 核算方式代码
     *
     * @param hsfsDm
     *            the hsfsDm to set
     */
    public void setHsfsDm(String hsfsDm) {
        this.hsfsDm = (hsfsDm == null ? null : hsfsDm.trim());
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
     * 文化事业建设费缴费信息登记(1=是，0=否)
     *
     * @return the whsyjsfjfxxdj
     */
    public Boolean getWhsyjsfjfxxdj() {
        return whsyjsfjfxxdj;
    }

    /**
     * 文化事业建设费缴费信息登记(1=是，0=否)
     *
     * @param whsyjsfjfxxdj
     *            the whsyjsfjfxxdj to set
     */
    public void setWhsyjsfjfxxdj(Boolean whsyjsfjfxxdj) {
        this.whsyjsfjfxxdj = whsyjsfjfxxdj;
    }

    /**
     * 增值税企业类型代码
     *
     * @return the zzsqylxDm
     */
    public String getZzsqylxDm() {
        return zzsqylxDm;
    }

    /**
     * 增值税企业类型代码
     *
     * @param zzsqylxDm
     *            the zzsqylxDm to set
     */
    public void setZzsqylxDm(String zzsqylxDm) {
        this.zzsqylxDm = (zzsqylxDm == null ? null : zzsqylxDm.trim());
    }

    /**
     * 营改增纳税人类型代码
     *
     * @return the ygznsrlxDm
     */
    public String getYgznsrlxDm() {
        return ygznsrlxDm;
    }

    /**
     * 营改增纳税人类型代码
     *
     * @param ygznsrlxDm
     *            the ygznsrlxDm to set
     */
    public void setYgznsrlxDm(String ygznsrlxDm) {
        this.ygznsrlxDm = (ygznsrlxDm == null ? null : ygznsrlxDm.trim());
    }

    /**
     * 主税种征收项目代码
     *
     * @return the zszZsxmDm
     */
    public String getZszZsxmDm() {
        return zszZsxmDm;
    }

    /**
     * 主税种征收项目代码
     *
     * @param zszZsxmDm
     *            the zszZsxmDm to set
     */
    public void setZszZsxmDm(String zszZsxmDm) {
        this.zszZsxmDm = (zszZsxmDm == null ? null : zszZsxmDm.trim());
    }

    /**
     * 企业所得税征收方式代码
     *
     * @return the qysdszsfsDm
     */
    public String getQysdszsfsDm() {
        return qysdszsfsDm;
    }

    /**
     * 企业所得税征收方式代码
     *
     * @param qysdszsfsDm
     *            the qysdszsfsDm to set
     */
    public void setQysdszsfsDm(String qysdszsfsDm) {
        this.qysdszsfsDm = (qysdszsfsDm == null ? null : qysdszsfsDm.trim());
    }

    /**
     * 纳税人corpid(废弃)
     *
     * @return the corpId
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     * 纳税人corpid(废弃)
     *
     * @param corpId
     *            the corpId to set
     */
    public void setCorpId(String corpId) {
        this.corpId = (corpId == null ? null : corpId.trim());
    }

    /**
     * 删除标记
     *
     * @return the isDelete
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 删除标记
     *
     * @param isDelete
     *            the isDelete to set
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
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
    public Date getCjsj() {
        return cjsj;
    }

    /**
     * 创建时间
     *
     * @param cjsj
     *            the cjsj to set
     */
    public void setCjsj(Date cjsj) {
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
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * 修改时间
     *
     * @param xgsj
     *            the xgsj to set
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
        result = prime * result + ((getBmId() == null) ? 0 : getBmId().hashCode());
        result = prime * result + ((getDjxh() == null) ? 0 : getDjxh().hashCode());
        result = prime * result + ((getNsrsbh() == null) ? 0 : getNsrsbh().hashCode());
        result = prime * result + ((getShxydm() == null) ? 0 : getShxydm().hashCode());
        result = prime * result + ((getShxydm6() == null) ? 0 : getShxydm6().hashCode());
        result = prime * result + ((getNsrmc() == null) ? 0 : getNsrmc().hashCode());
        result = prime * result + ((getNsrjc() == null) ? 0 : getNsrjc().hashCode());
        result = prime * result + ((getSjshxydm() == null) ? 0 : getSjshxydm().hashCode());
        result = prime * result + ((getNsrztDm() == null) ? 0 : getNsrztDm().hashCode());
        result = prime * result + ((getJhbj() == null) ? 0 : getJhbj().hashCode());
        result = prime * result + ((getZdsyhbj() == null) ? 0 : getZdsyhbj().hashCode());
        result = prime * result + ((getYxcys() == null) ? 0 : getYxcys().hashCode());
        result = prime * result + ((getZggsjgDm() == null) ? 0 : getZggsjgDm().hashCode());
        result = prime * result + ((getZgdsjgDm() == null) ? 0 : getZgdsjgDm().hashCode());
        result = prime * result + ((getDjzclxDm() == null) ? 0 : getDjzclxDm().hashCode());
        result = prime * result + ((getParentDjzclxDm() == null) ? 0 : getParentDjzclxDm().hashCode());
        result = prime * result + ((getHyDm() == null) ? 0 : getHyDm().hashCode());
        result = prime * result + ((getZzzcbj() == null) ? 0 : getZzzcbj().hashCode());
        result = prime * result + ((getKhyh() == null) ? 0 : getKhyh().hashCode());
        result = prime * result + ((getYhzh() == null) ? 0 : getYhzh().hashCode());
        result = prime * result + ((getZcdz() == null) ? 0 : getZcdz().hashCode());
        result = prime * result + ((getZcdlxdh() == null) ? 0 : getZcdlxdh().hashCode());
        result = prime * result + ((getScjydz() == null) ? 0 : getScjydz().hashCode());
        result = prime * result + ((getScjydlxdh() == null) ? 0 : getScjydlxdh().hashCode());
        result = prime * result + ((getQysdszsbj() == null) ? 0 : getQysdszsbj().hashCode());
        result = prime * result + ((getZzsnslx() == null) ? 0 : getZzsnslx().hashCode());
        result = prime * result + ((getDzxwqyBj() == null) ? 0 : getDzxwqyBj().hashCode());
        result = prime * result + ((getXxwlqyBj() == null) ? 0 : getXxwlqyBj().hashCode());
        result = prime * result + ((getNsxydj() == null) ? 0 : getNsxydj().hashCode());
        result = prime * result + ((getKzztdjlxDm() == null) ? 0 : getKzztdjlxDm().hashCode());
        result = prime * result + ((getJyfw() == null) ? 0 : getJyfw().hashCode());
        result = prime * result + ((getDjrq() == null) ? 0 : getDjrq().hashCode());
        result = prime * result + ((getKqccsztdj() == null) ? 0 : getKqccsztdj().hashCode());
        result = prime * result + ((getHsfsDm() == null) ? 0 : getHsfsDm().hashCode());
        result = prime * result + ((getKjzdzzDm() == null) ? 0 : getKjzdzzDm().hashCode());
        result = prime * result + ((getWhsyjsfjfxxdj() == null) ? 0 : getWhsyjsfjfxxdj().hashCode());
        result = prime * result + ((getZzsqylxDm() == null) ? 0 : getZzsqylxDm().hashCode());
        result = prime * result + ((getYgznsrlxDm() == null) ? 0 : getYgznsrlxDm().hashCode());
        result = prime * result + ((getZszZsxmDm() == null) ? 0 : getZszZsxmDm().hashCode());
        result = prime * result + ((getQysdszsfsDm() == null) ? 0 : getQysdszsfsDm().hashCode());
        result = prime * result + ((getCorpId() == null) ? 0 : getCorpId().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getCjr() == null) ? 0 : getCjr().hashCode());
        result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
        result = prime * result + ((getXgr() == null) ? 0 : getXgr().hashCode());
        result = prime * result + ((getXgsj() == null) ? 0 : getXgsj().hashCode());
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
        YhzxXnzzNsr other = (YhzxXnzzNsr) obj;
        return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
                && (this.getBmId() == null ? other.getBmId() == null : this.getBmId().equals(other.getBmId()))
                && (this.getDjxh() == null ? other.getDjxh() == null : this.getDjxh().equals(other.getDjxh()))
                && (this.getNsrsbh() == null ? other.getNsrsbh() == null : this.getNsrsbh().equals(other.getNsrsbh()))
                && (this.getShxydm() == null ? other.getShxydm() == null : this.getShxydm().equals(other.getShxydm()))
                && (this.getShxydm6() == null ? other.getShxydm6() == null : this.getShxydm6().equals(other.getShxydm6()))
                && (this.getNsrmc() == null ? other.getNsrmc() == null : this.getNsrmc().equals(other.getNsrmc()))
                && (this.getNsrjc() == null ? other.getNsrjc() == null : this.getNsrjc().equals(other.getNsrjc()))
                && (this.getSjshxydm() == null ? other.getSjshxydm() == null : this.getSjshxydm().equals(other.getSjshxydm()))
                && (this.getNsrztDm() == null ? other.getNsrztDm() == null : this.getNsrztDm().equals(other.getNsrztDm()))
                && (this.getJhbj() == null ? other.getJhbj() == null : this.getJhbj().equals(other.getJhbj()))
                && (this.getZdsyhbj() == null ? other.getZdsyhbj() == null : this.getZdsyhbj().equals(other.getZdsyhbj()))
                && (this.getYxcys() == null ? other.getYxcys() == null : this.getYxcys().equals(other.getYxcys()))
                && (this.getZggsjgDm() == null ? other.getZggsjgDm() == null : this.getZggsjgDm().equals(other.getZggsjgDm()))
                && (this.getZgdsjgDm() == null ? other.getZgdsjgDm() == null : this.getZgdsjgDm().equals(other.getZgdsjgDm()))
                && (this.getDjzclxDm() == null ? other.getDjzclxDm() == null : this.getDjzclxDm().equals(other.getDjzclxDm()))
                && (this.getParentDjzclxDm() == null ? other.getParentDjzclxDm() == null : this.getParentDjzclxDm().equals(other.getParentDjzclxDm()))
                && (this.getHyDm() == null ? other.getHyDm() == null : this.getHyDm().equals(other.getHyDm()))
                && (this.getZzzcbj() == null ? other.getZzzcbj() == null : this.getZzzcbj().equals(other.getZzzcbj()))
                && (this.getKhyh() == null ? other.getKhyh() == null : this.getKhyh().equals(other.getKhyh()))
                && (this.getYhzh() == null ? other.getYhzh() == null : this.getYhzh().equals(other.getYhzh()))
                && (this.getZcdz() == null ? other.getZcdz() == null : this.getZcdz().equals(other.getZcdz()))
                && (this.getZcdlxdh() == null ? other.getZcdlxdh() == null : this.getZcdlxdh().equals(other.getZcdlxdh()))
                && (this.getScjydz() == null ? other.getScjydz() == null : this.getScjydz().equals(other.getScjydz()))
                && (this.getScjydlxdh() == null ? other.getScjydlxdh() == null : this.getScjydlxdh().equals(other.getScjydlxdh()))
                && (this.getQysdszsbj() == null ? other.getQysdszsbj() == null : this.getQysdszsbj().equals(other.getQysdszsbj()))
                && (this.getZzsnslx() == null ? other.getZzsnslx() == null : this.getZzsnslx().equals(other.getZzsnslx()))
                && (this.getDzxwqyBj() == null ? other.getDzxwqyBj() == null : this.getDzxwqyBj().equals(other.getDzxwqyBj()))
                && (this.getXxwlqyBj() == null ? other.getXxwlqyBj() == null : this.getXxwlqyBj().equals(other.getXxwlqyBj()))
                && (this.getNsxydj() == null ? other.getNsxydj() == null : this.getNsxydj().equals(other.getNsxydj()))
                && (this.getKzztdjlxDm() == null ? other.getKzztdjlxDm() == null : this.getKzztdjlxDm().equals(other.getKzztdjlxDm()))
                && (this.getJyfw() == null ? other.getJyfw() == null : this.getJyfw().equals(other.getJyfw()))
                && (this.getDjrq() == null ? other.getDjrq() == null : this.getDjrq().equals(other.getDjrq()))
                && (this.getKqccsztdj() == null ? other.getKqccsztdj() == null : this.getKqccsztdj().equals(other.getKqccsztdj()))
                && (this.getHsfsDm() == null ? other.getHsfsDm() == null : this.getHsfsDm().equals(other.getHsfsDm()))
                && (this.getKjzdzzDm() == null ? other.getKjzdzzDm() == null : this.getKjzdzzDm().equals(other.getKjzdzzDm()))
                && (this.getWhsyjsfjfxxdj() == null ? other.getWhsyjsfjfxxdj() == null : this.getWhsyjsfjfxxdj().equals(other.getWhsyjsfjfxxdj()))
                && (this.getZzsqylxDm() == null ? other.getZzsqylxDm() == null : this.getZzsqylxDm().equals(other.getZzsqylxDm()))
                && (this.getYgznsrlxDm() == null ? other.getYgznsrlxDm() == null : this.getYgznsrlxDm().equals(other.getYgznsrlxDm()))
                && (this.getZszZsxmDm() == null ? other.getZszZsxmDm() == null : this.getZszZsxmDm().equals(other.getZszZsxmDm()))
                && (this.getQysdszsfsDm() == null ? other.getQysdszsfsDm() == null : this.getQysdszsfsDm().equals(other.getQysdszsfsDm()))
                && (this.getCorpId() == null ? other.getCorpId() == null : this.getCorpId().equals(other.getCorpId()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
                && (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
                && (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()))
                && (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()));
    }
}