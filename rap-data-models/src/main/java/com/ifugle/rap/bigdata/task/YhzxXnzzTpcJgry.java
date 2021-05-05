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
public class YhzxXnzzTpcJgry extends EnhanceModel<Long> {
	private static final long serialVersionUID = 1L;

	/**
	 * 虚拟组织ID
	 */
	@NotNull
	@Label("虚拟组织ID")
	private Long xnzzId;

	/**
	 * 虚拟组织用户表关联ID
	 */
	@Label("虚拟组织用户表关联ID")
	private Long yhId;

	/**
	 * 主管机关代码
	 */
	@NotNull
	@Size(max = 11)
	@Label("主管机关代码")
	private String zgjgDm;

	/**
	 * 机构类型(1-税务局;后续接入其他机构,再定义。)
	 */
	@NotNull
	@Label("机构类型")
	private Byte jglx;

	/**
	 * 机构部门代码
	 */
	@NotNull
	@Size(max = 11)
	@Label("机构部门代码")
	private String jgbmDm;

	/**
	 * 机构人员代码(同步过来的税务人员代码)
	 */
	@NotNull
	@Size(max = 11)
	@Label("机构人员代码")
	private String jgryDm;

	/**
	 * 姓名#CryptSimple#
	 */
	@NotNull
	@Size(max = 100)
	@Label("姓名")
	private String xm;

	/**
	 * 职位#CryptSimple#
	 */
	@Size(max = 300)
	@Label("职位")
	private String zw;

	/**
	 * 手机号码#CryptNumber#
	 */
	@NotNull
	@Size(max = 30)
	@Label("手机号码")
	private String sjhm;

	/**
	 * 证件类型
	 */
	@Size(max = 3)
	@Label("证件类型")
	private String zjlx;

	/**
	 * 证件号码#CryptBase36#
	 */
	@Size(max = 30)
	@Label("证件号码")
	private String zjhm;

	/**
	 * 同步状态(是否已同步到钉钉0=未同步,1=已同步)
	 */
	@Label("同步状态")
	private Byte tbzt;

	/**
	 * 创建时间
	 */
	@Label("创建时间")
	private Date cjsj;

	/**
	 * 创建人
	 */
	@NotNull
	@Size(max = 50)
	@Label("创建人")
	private String cjr;

	/**
	 * 修改时间
	 */
	@Label("修改时间")
	private Date xgsj;

	/**
	 * 修改人
	 */
	@NotNull
	@Size(max = 50)
	@Label("修改人")
	private String xgr;

	/**
	 * 删除标记(0=否，1=是)
	 */
	@Label("删除标记")
	private Byte isDelete;

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
	 * 虚拟组织用户表关联ID
	 * 
	 * @return the yhId
	 */
	public Long getYhId() {
		return yhId;
	}

	/**
	 * 虚拟组织用户表关联ID
	 * 
	 * @param yhId
	 *            the yhId to set
	 */
	public void setYhId(Long yhId) {
		this.yhId = yhId;
	}

	/**
	 * 主管机关代码
	 * 
	 * @return the zgjgDm
	 */
	public String getZgjgDm() {
		return zgjgDm;
	}

	/**
	 * 主管机关代码
	 * 
	 * @param zgjgDm
	 *            the zgjgDm to set
	 */
	public void setZgjgDm(String zgjgDm) {
		this.zgjgDm = (zgjgDm == null ? null : zgjgDm.trim());
	}

	/**
	 * 机构类型(1-税务局;后续接入其他机构,再定义。)
	 * 
	 * @return the jglx
	 */
	public Byte getJglx() {
		return jglx;
	}

	/**
	 * 机构类型(1-税务局;后续接入其他机构,再定义。)
	 * 
	 * @param jglx
	 *            the jglx to set
	 */
	public void setJglx(Byte jglx) {
		this.jglx = jglx;
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
	 * 机构人员代码(同步过来的税务人员代码)
	 * 
	 * @return the jgryDm
	 */
	public String getJgryDm() {
		return jgryDm;
	}

	/**
	 * 机构人员代码(同步过来的税务人员代码)
	 * 
	 * @param jgryDm
	 *            the jgryDm to set
	 */
	public void setJgryDm(String jgryDm) {
		this.jgryDm = (jgryDm == null ? null : jgryDm.trim());
	}

	/**
	 * 姓名#CryptSimple#
	 * 
	 * @return the xm
	 */
	public String getXm() {
		return xm;
	}

	/**
	 * 姓名#CryptSimple#
	 * 
	 * @param xm
	 *            the xm to set
	 */
	public void setXm(String xm) {
		this.xm = (xm == null ? null : xm.trim());
	}

	/**
	 * 职位#CryptSimple#
	 * 
	 * @return the zw
	 */
	public String getZw() {
		return zw;
	}

	/**
	 * 职位#CryptSimple#
	 * 
	 * @param zw
	 *            the zw to set
	 */
	public void setZw(String zw) {
		this.zw = (zw == null ? null : zw.trim());
	}

	/**
	 * 手机号码#CryptNumber#
	 * 
	 * @return the sjhm
	 */
	public String getSjhm() {
		return sjhm;
	}

	/**
	 * 手机号码#CryptNumber#
	 * 
	 * @param sjhm
	 *            the sjhm to set
	 */
	public void setSjhm(String sjhm) {
		this.sjhm = (sjhm == null ? null : sjhm.trim());
	}

	/**
	 * 证件类型
	 * 
	 * @return the zjlx
	 */
	public String getZjlx() {
		return zjlx;
	}

	/**
	 * 证件类型
	 * 
	 * @param zjlx
	 *            the zjlx to set
	 */
	public void setZjlx(String zjlx) {
		this.zjlx = (zjlx == null ? null : zjlx.trim());
	}

	/**
	 * 证件号码#CryptBase36#
	 * 
	 * @return the zjhm
	 */
	public String getZjhm() {
		return zjhm;
	}

	/**
	 * 证件号码#CryptBase36#
	 * 
	 * @param zjhm
	 *            the zjhm to set
	 */
	public void setZjhm(String zjhm) {
		this.zjhm = (zjhm == null ? null : zjhm.trim());
	}

	/**
	 * 同步状态(是否已同步到钉钉0=未同步,1=已同步)
	 * 
	 * @return the tbzt
	 */
	public Byte getTbzt() {
		return tbzt;
	}

	/**
	 * 同步状态(是否已同步到钉钉0=未同步,1=已同步)
	 * 
	 * @param tbzt
	 *            the tbzt to set
	 */
	public void setTbzt(Byte tbzt) {
		this.tbzt = tbzt;
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
	 * 删除标记(0=否，1=是)
	 * 
	 * @return the isDelete
	 */
	public Byte getIsDelete() {
		return isDelete;
	}

	/**
	 * 删除标记(0=否，1=是)
	 * 
	 * @param isDelete
	 *            the isDelete to set
	 */
	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
		result = prime * result + ((getYhId() == null) ? 0 : getYhId().hashCode());
		result = prime * result + ((getZgjgDm() == null) ? 0 : getZgjgDm().hashCode());
		result = prime * result + ((getJglx() == null) ? 0 : getJglx().hashCode());
		result = prime * result + ((getJgbmDm() == null) ? 0 : getJgbmDm().hashCode());
		result = prime * result + ((getJgryDm() == null) ? 0 : getJgryDm().hashCode());
		result = prime * result + ((getXm() == null) ? 0 : getXm().hashCode());
		result = prime * result + ((getZw() == null) ? 0 : getZw().hashCode());
		result = prime * result + ((getSjhm() == null) ? 0 : getSjhm().hashCode());
		result = prime * result + ((getZjlx() == null) ? 0 : getZjlx().hashCode());
		result = prime * result + ((getZjhm() == null) ? 0 : getZjhm().hashCode());
		result = prime * result + ((getTbzt() == null) ? 0 : getTbzt().hashCode());
		result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
		result = prime * result + ((getCjr() == null) ? 0 : getCjr().hashCode());
		result = prime * result + ((getXgsj() == null) ? 0 : getXgsj().hashCode());
		result = prime * result + ((getXgr() == null) ? 0 : getXgr().hashCode());
		result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
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
		YhzxXnzzTpcJgry other = (YhzxXnzzTpcJgry) obj;
		return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
				&& (this.getYhId() == null ? other.getYhId() == null : this.getYhId().equals(other.getYhId()))
				&& (this.getZgjgDm() == null ? other.getZgjgDm() == null : this.getZgjgDm().equals(other.getZgjgDm()))
				&& (this.getJglx() == null ? other.getJglx() == null : this.getJglx().equals(other.getJglx()))
				&& (this.getJgbmDm() == null ? other.getJgbmDm() == null : this.getJgbmDm().equals(other.getJgbmDm()))
				&& (this.getJgryDm() == null ? other.getJgryDm() == null : this.getJgryDm().equals(other.getJgryDm()))
				&& (this.getXm() == null ? other.getXm() == null : this.getXm().equals(other.getXm()))
				&& (this.getZw() == null ? other.getZw() == null : this.getZw().equals(other.getZw()))
				&& (this.getSjhm() == null ? other.getSjhm() == null : this.getSjhm().equals(other.getSjhm()))
				&& (this.getZjlx() == null ? other.getZjlx() == null : this.getZjlx().equals(other.getZjlx()))
				&& (this.getZjhm() == null ? other.getZjhm() == null : this.getZjhm().equals(other.getZjhm()))
				&& (this.getTbzt() == null ? other.getTbzt() == null : this.getTbzt().equals(other.getTbzt()))
				&& (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
				&& (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
				&& (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()))
				&& (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()))
				&& (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
	}
}