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
public class YhzxXnzzYhNsr extends EnhanceModel<Long> {
	private static final long serialVersionUID = 1L;

	/**
	 * 虚拟组织ID
	 */
	@NotNull
	@Label("虚拟组织ID")
	private Long xnzzId;

	/**
	 * 用户ID(YHZX_XNZZ_YH.ID)
	 */
	@NotNull
	@Label("用户ID")
	private Long yhId;

	/**
	 * 部门ID(YHZX_XNZZ_BM.ID)
	 */
	@NotNull
	@Label("部门ID")
	private Long bmId;

	/**
	 * 钉钉的部门ID
	 */
	@Size(max = 50)
	@Label("钉钉的部门ID")
	private String deptId;

	/**
	 * 成员属性1、税务局部门人员2、普通纳税人人员4、第三方服务机构人员
	 */
	@NotNull
	@Label("成员属性1、税务局部门人员2、普通纳税人人员4、第三方服务机构人员")
	private Byte cysx;

	/**
	 * 纳税人ID(YHZX_XNZZ_NSR.ID)
	 */
	@Label("纳税人ID")
	private Long nsrId;

	/**
	 * 成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员
	 */
	@Size(max = 2)
	@Label("成员类型 1: 法人 2: 财务负责人 3: 办税人 4: 其他办税人 5: 购票员")
	private String rylxDm;

	/**
	 * 购票员ID
	 */
	@Size(max = 50)
	@Label("购票员ID")
	private String gpyId;

	/**
	 * 认证级别(0=未认证，1=强制认证，2=短信认证，16=刷脸认证)
	 */
	@Label("认证级别")
	private Byte rzjb;

	/**
	 * 认证时间
	 */
	@Label("认证时间")
	private Date rzsj;

	/**
	 * 0=未同步 1=已同步
	 */
	@Label("0=未同步 1=已同步")
	private Byte tbzt;

	/**
	 * 显示顺序
	 */
	@Label("显示顺序")
	private Long xssx;

	/**
	 * 删除标记
	 */
	@Label("删除标记")
	private Boolean isDelete;

	/**
	 * 创建时间
	 */
	@Label("创建时间")
	private Date cjsj;

	/**
	 * 创建人
	 */
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
	@Size(max = 50)
	@Label("修改人")
	private String xgr;

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
	 * 用户ID(YHZX_XNZZ_YH.ID)
	 * 
	 * @return the yhId
	 */
	public Long getYhId() {
		return yhId;
	}

	/**
	 * 用户ID(YHZX_XNZZ_YH.ID)
	 * 
	 * @param yhId
	 *            the yhId to set
	 */
	public void setYhId(Long yhId) {
		this.yhId = yhId;
	}

	/**
	 * 部门ID(YHZX_XNZZ_BM.ID)
	 * 
	 * @return the bmId
	 */
	public Long getBmId() {
		return bmId;
	}

	/**
	 * 部门ID(YHZX_XNZZ_BM.ID)
	 * 
	 * @param bmId
	 *            the bmId to set
	 */
	public void setBmId(Long bmId) {
		this.bmId = bmId;
	}

	/**
	 * 钉钉的部门ID
	 * 
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * 钉钉的部门ID
	 * 
	 * @param deptId
	 *            the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = (deptId == null ? null : deptId.trim());
	}

	/**
	 * 成员属性1、税务局部门人员2、普通纳税人人员4、第三方服务机构人员
	 * 
	 * @return the cysx
	 */
	public Byte getCysx() {
		return cysx;
	}

	/**
	 * 成员属性1、税务局部门人员2、普通纳税人人员4、第三方服务机构人员
	 * 
	 * @param cysx
	 *            the cysx to set
	 */
	public void setCysx(Byte cysx) {
		this.cysx = cysx;
	}

	/**
	 * 纳税人ID(YHZX_XNZZ_NSR.ID)
	 * 
	 * @return the nsrId
	 */
	public Long getNsrId() {
		return nsrId;
	}

	/**
	 * 纳税人ID(YHZX_XNZZ_NSR.ID)
	 * 
	 * @param nsrId
	 *            the nsrId to set
	 */
	public void setNsrId(Long nsrId) {
		this.nsrId = nsrId;
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
	 * 认证级别(0=未认证，1=强制认证，2=短信认证，16=刷脸认证)
	 * 
	 * @return the rzjb
	 */
	public Byte getRzjb() {
		return rzjb;
	}

	/**
	 * 认证级别(0=未认证，1=强制认证，2=短信认证，16=刷脸认证)
	 * 
	 * @param rzjb
	 *            the rzjb to set
	 */
	public void setRzjb(Byte rzjb) {
		this.rzjb = rzjb;
	}

	/**
	 * 认证时间
	 * 
	 * @return the rzsj
	 */
	public Date getRzsj() {
		return rzsj;
	}

	/**
	 * 认证时间
	 * 
	 * @param rzsj
	 *            the rzsj to set
	 */
	public void setRzsj(Date rzsj) {
		this.rzsj = rzsj;
	}

	/**
	 * 0=未同步 1=已同步
	 * 
	 * @return the tbzt
	 */
	public Byte getTbzt() {
		return tbzt;
	}

	/**
	 * 0=未同步 1=已同步
	 * 
	 * @param tbzt
	 *            the tbzt to set
	 */
	public void setTbzt(Byte tbzt) {
		this.tbzt = tbzt;
	}

	/**
	 * 显示顺序
	 * 
	 * @return the xssx
	 */
	public Long getXssx() {
		return xssx;
	}

	/**
	 * 显示顺序
	 * 
	 * @param xssx
	 *            the xssx to set
	 */
	public void setXssx(Long xssx) {
		this.xssx = xssx;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getXnzzId() == null) ? 0 : getXnzzId().hashCode());
		result = prime * result + ((getYhId() == null) ? 0 : getYhId().hashCode());
		result = prime * result + ((getBmId() == null) ? 0 : getBmId().hashCode());
		result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
		result = prime * result + ((getCysx() == null) ? 0 : getCysx().hashCode());
		result = prime * result + ((getNsrId() == null) ? 0 : getNsrId().hashCode());
		result = prime * result + ((getRylxDm() == null) ? 0 : getRylxDm().hashCode());
		result = prime * result + ((getGpyId() == null) ? 0 : getGpyId().hashCode());
		result = prime * result + ((getRzjb() == null) ? 0 : getRzjb().hashCode());
		result = prime * result + ((getRzsj() == null) ? 0 : getRzsj().hashCode());
		result = prime * result + ((getTbzt() == null) ? 0 : getTbzt().hashCode());
		result = prime * result + ((getXssx() == null) ? 0 : getXssx().hashCode());
		result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
		result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
		result = prime * result + ((getCjr() == null) ? 0 : getCjr().hashCode());
		result = prime * result + ((getXgsj() == null) ? 0 : getXgsj().hashCode());
		result = prime * result + ((getXgr() == null) ? 0 : getXgr().hashCode());
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
		YhzxXnzzYhNsr other = (YhzxXnzzYhNsr) obj;
		return (this.getXnzzId() == null ? other.getXnzzId() == null : this.getXnzzId().equals(other.getXnzzId()))
				&& (this.getYhId() == null ? other.getYhId() == null : this.getYhId().equals(other.getYhId()))
				&& (this.getBmId() == null ? other.getBmId() == null : this.getBmId().equals(other.getBmId()))
				&& (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
				&& (this.getCysx() == null ? other.getCysx() == null : this.getCysx().equals(other.getCysx()))
				&& (this.getNsrId() == null ? other.getNsrId() == null : this.getNsrId().equals(other.getNsrId()))
				&& (this.getRylxDm() == null ? other.getRylxDm() == null : this.getRylxDm().equals(other.getRylxDm()))
				&& (this.getGpyId() == null ? other.getGpyId() == null : this.getGpyId().equals(other.getGpyId()))
				&& (this.getRzjb() == null ? other.getRzjb() == null : this.getRzjb().equals(other.getRzjb()))
				&& (this.getRzsj() == null ? other.getRzsj() == null : this.getRzsj().equals(other.getRzsj()))
				&& (this.getTbzt() == null ? other.getTbzt() == null : this.getTbzt().equals(other.getTbzt()))
				&& (this.getXssx() == null ? other.getXssx() == null : this.getXssx().equals(other.getXssx()))
				&& (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
				&& (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
				&& (this.getCjr() == null ? other.getCjr() == null : this.getCjr().equals(other.getCjr()))
				&& (this.getXgsj() == null ? other.getXgsj() == null : this.getXgsj().equals(other.getXgsj()))
				&& (this.getXgr() == null ? other.getXgr() == null : this.getXgr().equals(other.getXgr()));
	}
}