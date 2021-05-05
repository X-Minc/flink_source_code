/**
 * Copyright(C) 2019 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 * 
 */
package com.ifugle.rap.bigdata.task;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ifugle.rap.core.annotation.Label;
import com.ifugle.rap.core.model.impl.EnhanceModel;

/**
 * @since 2019-07-23 11:40:11
 * @version $Id$
 * @author xuweigang
 */
public class YhzxXnzzBm extends EnhanceModel<Long> {
	private static final long serialVersionUID = 1L;

	/**
	 * 虚拟组织ID
	 */
	@NotNull
	@Label("虚拟组织ID")
	private Long xnzzId;

	/**
	 * 父部门id，根部门id为1
	 */
	@Label("父部门id，根部门id为1")
	private Long parentId;

	/**
	 * 部门id（钉钉）
	 */
	@Size(max = 50)
	@Label("部门id（钉钉）")
	private String deptId;

	/**
	 * 部门名称
	 */
	@NotNull
	@Size(max = 300)
	@Label("部门名称")
	private String bmmc;

	/**
	 * 上级部门id（钉钉）
	 */
	@Size(max = 50)
	@Label("上级部门id（钉钉）")
	private String parentDeptId;

	/**
	 * 主管税务科所代码
	 */
	@Label("主管税务科所代码")
	private Long swjgDm;

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
	 * 部门属性1、政府机构2、纳税人4、服务机构
	 */
	@NotNull
	@Label("部门属性1、政府机构2、纳税人4、服务机构")
	private Byte bmsx;

	/**
	 * 信息脱敏标记(0=不脱敏，1=脱敏)
	 */
	@Label("信息脱敏标记")
	private Byte xxtmbj;

	/**
	 * 部门成员上限（废弃）
	 */
	@Label("部门成员上限（废弃）")
	private Integer bmcysx;

	/**
	 * 在父部门次序值
	 */
	@Label("在父部门次序值")
	private Long xssx;

	/**
	 * 管户标记（0=否，1=是）
	 */
	@Label("管户标记（0=否，1=是）")
	private Byte ghbj;

	/**
	 * 部门用户数
	 */
	@Label("部门用户数")
	private Integer yhs;

	/**
	 * 部门企业数
	 */
	@Label("部门企业数")
	private Integer qys;

	/**
	 * 部门路径（上级部门id用-隔开）
	 */
	@Size(max = 2000)
	@Label("部门路径（上级部门id用-隔开）")
	private String bmPath;

	/**
	 * 部门名称路径（上级部门名称用-隔开）
	 */
	@Size(max = 2000)
	@Label("部门名称路径（上级部门名称用-隔开）")
	private String bmmcPath;

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
	 * 删除标记（0=否，1=是）
	 */
	@Label("删除标记（0=否，1=是）")
	private Byte isDelete;

	/**
	 * 税务机关标志(0-机关;1-部门)
	 */
	@Label("税务机关标志")
	private Byte swjgbz;

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
	 * 父部门id，根部门id为1
	 * 
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 父部门id，根部门id为1
	 * 
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 部门id（钉钉）
	 * 
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * 部门id（钉钉）
	 * 
	 * @param deptId
	 *            the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = (deptId == null ? null : deptId.trim());
	}

	/**
	 * 部门名称
	 * 
	 * @return the bmmc
	 */
	public String getBmmc() {
		return bmmc;
	}

	/**
	 * 部门名称
	 * 
	 * @param bmmc
	 *            the bmmc to set
	 */
	public void setBmmc(String bmmc) {
		this.bmmc = (bmmc == null ? null : bmmc.trim());
	}

	/**
	 * 上级部门id（钉钉）
	 * 
	 * @return the parentDeptId
	 */
	public String getParentDeptId() {
		return parentDeptId;
	}

	/**
	 * 上级部门id（钉钉）
	 * 
	 * @param parentDeptId
	 *            the parentDeptId to set
	 */
	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = (parentDeptId == null ? null : parentDeptId.trim());
	}

	/**
	 * 主管税务科所代码
	 * 
	 * @return the swjgDm
	 */
	public Long getSwjgDm() {
		return swjgDm;
	}

	/**
	 * 主管税务科所代码
	 * 
	 * @param swjgDm
	 *            the swjgDm to set
	 */
	public void setSwjgDm(Long swjgDm) {
		this.swjgDm = swjgDm;
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
	 * 部门属性1、政府机构2、纳税人4、服务机构
	 * 
	 * @return the bmsx
	 */
	public Byte getBmsx() {
		return bmsx;
	}

	/**
	 * 部门属性1、政府机构2、纳税人4、服务机构
	 * 
	 * @param bmsx
	 *            the bmsx to set
	 */
	public void setBmsx(Byte bmsx) {
		this.bmsx = bmsx;
	}

	/**
	 * 信息脱敏标记(0=不脱敏，1=脱敏)
	 * 
	 * @return the xxtmbj
	 */
	public Byte getXxtmbj() {
		return xxtmbj;
	}

	/**
	 * 信息脱敏标记(0=不脱敏，1=脱敏)
	 * 
	 * @param xxtmbj
	 *            the xxtmbj to set
	 */
	public void setXxtmbj(Byte xxtmbj) {
		this.xxtmbj = xxtmbj;
	}

	/**
	 * 部门成员上限（废弃）
	 * 
	 * @return the bmcysx
	 */
	public Integer getBmcysx() {
		return bmcysx;
	}

	/**
	 * 部门成员上限（废弃）
	 * 
	 * @param bmcysx
	 *            the bmcysx to set
	 */
	public void setBmcysx(Integer bmcysx) {
		this.bmcysx = bmcysx;
	}

	/**
	 * 在父部门次序值
	 * 
	 * @return the xssx
	 */
	public Long getXssx() {
		return xssx;
	}

	/**
	 * 在父部门次序值
	 * 
	 * @param xssx
	 *            the xssx to set
	 */
	public void setXssx(Long xssx) {
		this.xssx = xssx;
	}

	/**
	 * 管户标记（0=否，1=是）
	 * 
	 * @return the ghbj
	 */
	public Byte getGhbj() {
		return ghbj;
	}

	/**
	 * 管户标记（0=否，1=是）
	 * 
	 * @param ghbj
	 *            the ghbj to set
	 */
	public void setGhbj(Byte ghbj) {
		this.ghbj = ghbj;
	}

	/**
	 * 部门用户数
	 * 
	 * @return the yhs
	 */
	public Integer getYhs() {
		return yhs;
	}

	/**
	 * 部门用户数
	 * 
	 * @param yhs
	 *            the yhs to set
	 */
	public void setYhs(Integer yhs) {
		this.yhs = yhs;
	}

	/**
	 * 部门企业数
	 * 
	 * @return the qys
	 */
	public Integer getQys() {
		return qys;
	}

	/**
	 * 部门企业数
	 * 
	 * @param qys
	 *            the qys to set
	 */
	public void setQys(Integer qys) {
		this.qys = qys;
	}

	/**
	 * 部门路径（上级部门id用-隔开）
	 * 
	 * @return the bmPath
	 */
	public String getBmPath() {
		return bmPath;
	}

	/**
	 * 部门路径（上级部门id用-隔开）
	 * 
	 * @param bmPath
	 *            the bmPath to set
	 */
	public void setBmPath(String bmPath) {
		this.bmPath = (bmPath == null ? null : bmPath.trim());
	}

	/**
	 * 部门名称路径（上级部门名称用-隔开）
	 * 
	 * @return the bmmcPath
	 */
	public String getBmmcPath() {
		return bmmcPath;
	}

	/**
	 * 部门名称路径（上级部门名称用-隔开）
	 * 
	 * @param bmmcPath
	 *            the bmmcPath to set
	 */
	public void setBmmcPath(String bmmcPath) {
		this.bmmcPath = (bmmcPath == null ? null : bmmcPath.trim());
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

	/**
	 * 删除标记（0=否，1=是）
	 * 
	 * @return the isDelete
	 */
	public Byte getIsDelete() {
		return isDelete;
	}

	/**
	 * 删除标记（0=否，1=是）
	 * 
	 * @param isDelete
	 *            the isDelete to set
	 */
	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 税务机关标志(0-机关;1-部门)
	 * 
	 * @return the swjgbz
	 */
	public Byte getSwjgbz() {
		return swjgbz;
	}

	/**
	 * 税务机关标志(0-机关;1-部门)
	 * 
	 * @param swjgbz
	 *            the swjgbz to set
	 */
	public void setSwjgbz(Byte swjgbz) {
		this.swjgbz = swjgbz;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
		result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
		result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
		result = prime * result + ((getBmmc() == null) ? 0 : getBmmc().hashCode());
		result = prime * result + ((getParentDeptId() == null) ? 0 : getParentDeptId().hashCode());
		result = prime * result + ((getSwjgDm() == null) ? 0 : getSwjgDm().hashCode());
		result = prime * result + ((getZggsjgDm() == null) ? 0 : getZggsjgDm().hashCode());
		result = prime * result + ((getZgdsjgDm() == null) ? 0 : getZgdsjgDm().hashCode());
		result = prime * result + ((getBmsx() == null) ? 0 : getBmsx().hashCode());
		result = prime * result + ((getXxtmbj() == null) ? 0 : getXxtmbj().hashCode());
		result = prime * result + ((getBmcysx() == null) ? 0 : getBmcysx().hashCode());
		result = prime * result + ((getXssx() == null) ? 0 : getXssx().hashCode());
		result = prime * result + ((getGhbj() == null) ? 0 : getGhbj().hashCode());
		result = prime * result + ((getYhs() == null) ? 0 : getYhs().hashCode());
		result = prime * result + ((getQys() == null) ? 0 : getQys().hashCode());
		result = prime * result + ((getBmPath() == null) ? 0 : getBmPath().hashCode());
		result = prime * result + ((getBmmcPath() == null) ? 0 : getBmmcPath().hashCode());
		result = prime * result + ((getCjr() == null) ? 0 : getCjr().hashCode());
		result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
		result = prime * result + ((getXgr() == null) ? 0 : getXgr().hashCode());
		result = prime * result + ((getXgsj() == null) ? 0 : getXgsj().hashCode());
		result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
		result = prime * result + ((getSwjgbz() == null) ? 0 : getSwjgbz().hashCode());
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
		YhzxXnzzBm other = (YhzxXnzzBm) obj;
		return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
				&& (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
				&& (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
				&& (this.getBmmc() == null ? other.getBmmc() == null : this.getBmmc().equals(other.getBmmc()))
				&& (this.getParentDeptId() == null ? other.getParentDeptId() == null : this.getParentDeptId().equals(other.getParentDeptId()))
				&& (this.getSwjgDm() == null ? other.getSwjgDm() == null : this.getSwjgDm().equals(other.getSwjgDm()))
				&& (this.getZggsjgDm() == null ? other.getZggsjgDm() == null : this.getZggsjgDm().equals(other.getZggsjgDm()))
				&& (this.getZgdsjgDm() == null ? other.getZgdsjgDm() == null : this.getZgdsjgDm().equals(other.getZgdsjgDm()))
				&& (this.getBmsx() == null ? other.getBmsx() == null : this.getBmsx().equals(other.getBmsx()))
				&& (this.getXxtmbj() == null ? other.getXxtmbj() == null : this.getXxtmbj().equals(other.getXxtmbj()))
				&& (this.getBmcysx() == null ? other.getBmcysx() == null : this.getBmcysx().equals(other.getBmcysx()))
				&& (this.getXssx() == null ? other.getXssx() == null : this.getXssx().equals(other.getXssx()))
				&& (this.getGhbj() == null ? other.getGhbj() == null : this.getGhbj().equals(other.getGhbj()))
				&& (this.getYhs() == null ? other.getYhs() == null : this.getYhs().equals(other.getYhs()))
				&& (this.getQys() == null ? other.getQys() == null : this.getQys().equals(other.getQys()))
				&& (this.getBmPath() == null ? other.getBmPath() == null : this.getBmPath().equals(other.getBmPath()))
				&& (this.getBmmcPath() == null ? other.getBmmcPath() == null : this.getBmmcPath().equals(other.getBmmcPath()))
				&& (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
				&& (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
				&& (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()))
				&& (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()))
				&& (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
				&& (this.getSwjgbz() == null ? other.getSwjgbz() == null : this.getSwjgbz().equals(other.getSwjgbz()));
	}
}