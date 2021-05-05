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
 * @since 2019-08-12 13:48:29
 * @version $Id$
 * @author Administrator
 */
public class BiDmSwjg extends EnhanceModel<String> {
	private static final long serialVersionUID = 1L;

	/**
	 * 税务机关代码
	 */
	@NotNull
	@Size(max = 11)
	@Label("税务机关代码")
	private String swjgDm;

	/**
	 * 税务机关名称
	 */
	@NotNull
	@Size(max = 300)
	@Label("税务机关名称")
	private String swjgmc;

	/**
	 * 税务机关简称
	 */
	@Size(max = 150)
	@Label("税务机关简称")
	private String swjgjc;

	/**
	 * 税务机关类型 0跨组织 1虚拟组织 2虚拟组织部门
	 */
	@Label("税务机关类型 0跨组织 1虚拟组织 2虚拟组织部门")
	private Byte swjgbz;

	/**
	 * 上级税务机关代码
	 */
	@NotNull
	@Size(max = 11)
	@Label("上级税务机关代码")
	private String sjswjgDm;

	/**
	 * 税务机关代码层级全路径
	 */
	@Size(max = 300)
	@Label("税务机关代码层级全路径")
	private String swjgDmPath;

	/**
	 * 税务机关层级(0总局 1省局 2市局 3区县局 4分局片区)
	 */
	@Label("税务机关层级")
	private Byte swjgcj;

	/**
	 * 虚拟组织ID
	 */
	@Label("虚拟组织ID")
	private Long xnzzId;

	/**
	 * 虚拟组织名称
	 */
	@Size(max = 300)
	@Label("虚拟组织名称")
	private String xnzzmc;

	/**
	 * 部门ID列表，用【,】分割
	 */
	@Size(max = 200)
	@Label("部门ID列表，用【,】分割")
	private String bmIds;

	/**
	 * 行政区划数字代码
	 */
	@NotNull
	@Size(max = 6)
	@Label("行政区划数字代码")
	private String xzqhszDm;

	/**
	 * 启用标志(0未启用 1启用)
	 */
	@Label("启用标志")
	private Byte qybz;

	/**
	 * 启用日期
	 */
	@Label("启用日期")
	private Date qyrq;

	/**
	 * 本级是否显示标志(0不显示 1显示)
	 */
	@Label("本级是否显示标志")
	private Byte bjxsbz;

	/**
	 * 创建时间
	 */
	@Label("创建时间")
	private Date cjsj;

	/**
	 * 修改时间
	 */
	@Label("修改时间")
	private Date xgsj;

	/**
	 * 税务机关代码
	 * 
	 * @return the swjgDm
	 */
	public String getSwjgDm() {
		return swjgDm;
	}

	/**
	 * 税务机关代码
	 * 
	 * @param swjgDm
	 *            the swjgDm to set
	 */
	public void setSwjgDm(String swjgDm) {
		this.swjgDm = (swjgDm == null ? null : swjgDm.trim());
	}

	/**
	 * 税务机关名称
	 * 
	 * @return the swjgmc
	 */
	public String getSwjgmc() {
		return swjgmc;
	}

	/**
	 * 税务机关名称
	 * 
	 * @param swjgmc
	 *            the swjgmc to set
	 */
	public void setSwjgmc(String swjgmc) {
		this.swjgmc = (swjgmc == null ? null : swjgmc.trim());
	}

	/**
	 * 税务机关简称
	 * 
	 * @return the swjgjc
	 */
	public String getSwjgjc() {
		return swjgjc;
	}

	/**
	 * 税务机关简称
	 * 
	 * @param swjgjc
	 *            the swjgjc to set
	 */
	public void setSwjgjc(String swjgjc) {
		this.swjgjc = (swjgjc == null ? null : swjgjc.trim());
	}

	/**
	 * 税务机关类型 0跨组织 1虚拟组织 2虚拟组织部门
	 * 
	 * @return the swjgbz
	 */
	public Byte getSwjgbz() {
		return swjgbz;
	}

	/**
	 * 税务机关类型 0跨组织 1虚拟组织 2虚拟组织部门
	 * 
	 * @param swjgbz
	 *            the swjgbz to set
	 */
	public void setSwjgbz(Byte swjgbz) {
		this.swjgbz = swjgbz;
	}

	/**
	 * 上级税务机关代码
	 * 
	 * @return the sjswjgDm
	 */
	public String getSjswjgDm() {
		return sjswjgDm;
	}

	/**
	 * 上级税务机关代码
	 * 
	 * @param sjswjgDm
	 *            the sjswjgDm to set
	 */
	public void setSjswjgDm(String sjswjgDm) {
		this.sjswjgDm = (sjswjgDm == null ? null : sjswjgDm.trim());
	}

	/**
	 * 税务机关代码层级全路径
	 * 
	 * @return the swjgDmPath
	 */
	public String getSwjgDmPath() {
		return swjgDmPath;
	}

	/**
	 * 税务机关代码层级全路径
	 * 
	 * @param swjgDmPath
	 *            the swjgDmPath to set
	 */
	public void setSwjgDmPath(String swjgDmPath) {
		this.swjgDmPath = (swjgDmPath == null ? null : swjgDmPath.trim());
	}

	/**
	 * 税务机关层级(0总局 1省局 2市局 3区县局 4分局片区)
	 * 
	 * @return the swjgcj
	 */
	public Byte getSwjgcj() {
		return swjgcj;
	}

	/**
	 * 税务机关层级(0总局 1省局 2市局 3区县局 4分局片区)
	 * 
	 * @param swjgcj
	 *            the swjgcj to set
	 */
	public void setSwjgcj(Byte swjgcj) {
		this.swjgcj = swjgcj;
	}

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
	 * 虚拟组织名称
	 * 
	 * @return the xnzzmc
	 */
	public String getXnzzmc() {
		return xnzzmc;
	}

	/**
	 * 虚拟组织名称
	 * 
	 * @param xnzzmc
	 *            the xnzzmc to set
	 */
	public void setXnzzmc(String xnzzmc) {
		this.xnzzmc = (xnzzmc == null ? null : xnzzmc.trim());
	}

	/**
	 * 部门ID列表，用【,】分割
	 * 
	 * @return the bmIds
	 */
	public String getBmIds() {
		return bmIds;
	}

	/**
	 * 部门ID列表，用【,】分割
	 * 
	 * @param bmIds
	 *            the bmIds to set
	 */
	public void setBmIds(String bmIds) {
		this.bmIds = (bmIds == null ? null : bmIds.trim());
	}

	/**
	 * 行政区划数字代码
	 * 
	 * @return the xzqhszDm
	 */
	public String getXzqhszDm() {
		return xzqhszDm;
	}

	/**
	 * 行政区划数字代码
	 * 
	 * @param xzqhszDm
	 *            the xzqhszDm to set
	 */
	public void setXzqhszDm(String xzqhszDm) {
		this.xzqhszDm = (xzqhszDm == null ? null : xzqhszDm.trim());
	}

	/**
	 * 启用标志(0未启用 1启用)
	 * 
	 * @return the qybz
	 */
	public Byte getQybz() {
		return qybz;
	}

	/**
	 * 启用标志(0未启用 1启用)
	 * 
	 * @param qybz
	 *            the qybz to set
	 */
	public void setQybz(Byte qybz) {
		this.qybz = qybz;
	}

	/**
	 * 启用日期
	 * 
	 * @return the qyrq
	 */
	public Date getQyrq() {
		return qyrq;
	}

	/**
	 * 启用日期
	 * 
	 * @param qyrq
	 *            the qyrq to set
	 */
	public void setQyrq(Date qyrq) {
		this.qyrq = qyrq;
	}

	/**
	 * 本级是否显示标志(0不显示 1显示)
	 *
	 * @return the bjxsbz
	 */
	public Byte getBjxsbz() {
		return bjxsbz;
	}

	/**
	 * 本级是否显示标志(0不显示 1显示)
	 *
	 * @param bjxsbz
	 * 				the bjxsbz to set
	 */
	public void setBjxsbz(Byte bjxsbz) {
		this.bjxsbz = bjxsbz;
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
		result = prime * result + ((getSwjgDm() == null) ? 0 : getSwjgDm().hashCode());
		result = prime * result + ((getSwjgmc() == null) ? 0 : getSwjgmc().hashCode());
		result = prime * result + ((getSwjgjc() == null) ? 0 : getSwjgjc().hashCode());
		result = prime * result + ((getSwjgbz() == null) ? 0 : getSwjgbz().hashCode());
		result = prime * result + ((getSjswjgDm() == null) ? 0 : getSjswjgDm().hashCode());
		result = prime * result + ((getSwjgDmPath() == null) ? 0 : getSwjgDmPath().hashCode());
		result = prime * result + ((getSwjgcj() == null) ? 0 : getSwjgcj().hashCode());
		result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
		result = prime * result + ((getXnzzmc() == null) ? 0 : getXnzzmc().hashCode());
		result = prime * result + ((getBmIds() == null) ? 0 : getBmIds().hashCode());
		result = prime * result + ((getXzqhszDm() == null) ? 0 : getXzqhszDm().hashCode());
		result = prime * result + ((getQybz() == null) ? 0 : getQybz().hashCode());
		result = prime * result + ((getQyrq() == null) ? 0 : getQyrq().hashCode());
		result = prime * result + ((getBjxsbz() == null) ? 0 : getBjxsbz().hashCode());
		result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
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
		BiDmSwjg other = (BiDmSwjg) obj;
		return (this.getSwjgDm() == null ? other.getSwjgDm() == null : this.getSwjgDm().equals(other.getSwjgDm()))
				&& (this.getSwjgmc() == null ? other.getSwjgmc() == null : this.getSwjgmc().equals(other.getSwjgmc()))
				&& (this.getSwjgjc() == null ? other.getSwjgjc() == null : this.getSwjgjc().equals(other.getSwjgjc()))
				&& (this.getSwjgbz() == null ? other.getSwjgbz() == null : this.getSwjgbz().equals(other.getSwjgbz()))
				&& (this.getSjswjgDm() == null ? other.getSjswjgDm() == null : this.getSjswjgDm().equals(other.getSjswjgDm()))
				&& (this.getSwjgDmPath() == null ? other.getSwjgDmPath() == null : this.getSwjgDmPath().equals(other.getSwjgDmPath()))
				&& (this.getSwjgcj() == null ? other.getSwjgcj() == null : this.getSwjgcj().equals(other.getSwjgcj()))
				&& (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
				&& (this.getXnzzmc() == null ? other.getXnzzmc() == null : this.getXnzzmc().equals(other.getXnzzmc()))
				&& (this.getBmIds() == null ? other.getBmIds() == null : this.getBmIds().equals(other.getBmIds()))
				&& (this.getXzqhszDm() == null ? other.getXzqhszDm() == null : this.getXzqhszDm().equals(other.getXzqhszDm()))
				&& (this.getQybz() == null ? other.getQybz() == null : this.getQybz().equals(other.getQybz()))
				&& (this.getQyrq() == null ? other.getQyrq() == null : this.getQyrq().equals(other.getQyrq()))
				&& (this.getBjxsbz() == null ? other.getBjxsbz() == null : this.getBjxsbz().equals(other.getBjxsbz()))
				&& (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
				&& (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()));
	}
}