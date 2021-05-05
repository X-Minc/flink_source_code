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
public class YhzxXnzzTpcQyry extends EnhanceModel<Long> {
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
	 * 登记序号(源自金三登记表)
	 */
	@Size(max = 50)
	@Label("登记序号")
	private String djxh;

	/**
	 * 社会信用代码#CryptBase36#
	 */
	@NotNull
	@Size(max = 20)
	@Label("社会信用代码")
	private String shxydm;

	/**
	 * 纳税人识别号#CryptBase36#
	 */
	@NotNull
	@Size(max = 32)
	@Label("纳税人识别号")
	private String nsrsbh;

	/**
	 * 姓名#CryptSimple#
	 */
	@NotNull
	@Size(max = 100)
	@Label("姓名")
	private String xm;

	/**
	 * 手机号码#CryptNumber#
	 */
	@NotNull
	@Size(max = 30)
	@Label("手机号码")
	private String sjhm;

	/**
	 * 成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员
	 */
	@Size(max = 2)
	@Label("成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员")
	private String rylxDm;

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
	 * 购票员ID
	 */
	@Size(max = 50)
	@Label("购票员ID")
	private String gpyId;

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
	 * 成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员
	 * 
	 * @return the rylxDm
	 */
	public String getRylxDm() {
		return rylxDm;
	}

	/**
	 * 成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员
	 * 
	 * @param rylxDm
	 *            the rylxDm to set
	 */
	public void setRylxDm(String rylxDm) {
		this.rylxDm = (rylxDm == null ? null : rylxDm.trim());
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
	 * 购票员ID
	 * 
	 * @return the gpyId
	 */
	public String getGpyId() {
		return gpyId;
	}

	/**
	 * 购票员ID
	 * 
	 * @param gpyId
	 *            the gpyId to set
	 */
	public void setGpyId(String gpyId) {
		this.gpyId = (gpyId == null ? null : gpyId.trim());
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
		result = prime * result + ((getDjxh() == null) ? 0 : getDjxh().hashCode());
		result = prime * result + ((getShxydm() == null) ? 0 : getShxydm().hashCode());
		result = prime * result + ((getNsrsbh() == null) ? 0 : getNsrsbh().hashCode());
		result = prime * result + ((getXm() == null) ? 0 : getXm().hashCode());
		result = prime * result + ((getSjhm() == null) ? 0 : getSjhm().hashCode());
		result = prime * result + ((getRylxDm() == null) ? 0 : getRylxDm().hashCode());
		result = prime * result + ((getZjlx() == null) ? 0 : getZjlx().hashCode());
		result = prime * result + ((getZjhm() == null) ? 0 : getZjhm().hashCode());
		result = prime * result + ((getGpyId() == null) ? 0 : getGpyId().hashCode());
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
		YhzxXnzzTpcQyry other = (YhzxXnzzTpcQyry) obj;
		return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
				&& (this.getYhId() == null ? other.getYhId() == null : this.getYhId().equals(other.getYhId()))
				&& (this.getDjxh() == null ? other.getDjxh() == null : this.getDjxh().equals(other.getDjxh()))
				&& (this.getShxydm() == null ? other.getShxydm() == null : this.getShxydm().equals(other.getShxydm()))
				&& (this.getNsrsbh() == null ? other.getNsrsbh() == null : this.getNsrsbh().equals(other.getNsrsbh()))
				&& (this.getXm() == null ? other.getXm() == null : this.getXm().equals(other.getXm()))
				&& (this.getSjhm() == null ? other.getSjhm() == null : this.getSjhm().equals(other.getSjhm()))
				&& (this.getRylxDm() == null ? other.getRylxDm() == null : this.getRylxDm().equals(other.getRylxDm()))
				&& (this.getZjlx() == null ? other.getZjlx() == null : this.getZjlx().equals(other.getZjlx()))
				&& (this.getZjhm() == null ? other.getZjhm() == null : this.getZjhm().equals(other.getZjhm()))
				&& (this.getGpyId() == null ? other.getGpyId() == null : this.getGpyId().equals(other.getGpyId()))
				&& (this.getTbzt() == null ? other.getTbzt() == null : this.getTbzt().equals(other.getTbzt()))
				&& (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
				&& (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
				&& (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()))
				&& (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()))
				&& (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
	}
}