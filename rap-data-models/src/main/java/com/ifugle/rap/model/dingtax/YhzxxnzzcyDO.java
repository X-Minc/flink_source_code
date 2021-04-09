package com.ifugle.rap.model.dingtax;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

public class YhzxxnzzcyDO extends EnhanceModel<Long> {
    private Long id;

    private Long xnzzId;

    private Long bmId;

    private Long nsrId;

    private String dingid;

    private String userid;

    private String xm;

    private String sjhm;

    private String rylxDm;

    private Byte cysx;

    private Byte glybj;

    private Byte zbrybj;

    private String bz;

    private Byte cyzt;

    private Byte dxzt;

    private Date dxfssj;

    private String dxcwxx;

    private Date cjsj;

    private String cjr;

    private Date xgsj;

    private String xgr;

    private String zw;

    private Byte zcfsbj;

    private Byte zcrzjb;

    private Byte qyglybj;

    private Long xssx;

    private String zjlx;

    private String zjhm;

    private String gpyId;

    private Date jhsj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getXnzzId() {
        return xnzzId;
    }

    public void setXnzzId(Long xnzzId) {
        this.xnzzId = xnzzId;
    }

    public Long getBmId() {
        return bmId;
    }

    public void setBmId(Long bmId) {
        this.bmId = bmId;
    }

    public Long getNsrId() {
        return nsrId;
    }

    public void setNsrId(Long nsrId) {
        this.nsrId = nsrId;
    }

    public String getDingid() {
        return dingid;
    }

    public void setDingid(String dingid) {
        this.dingid = dingid == null ? null : dingid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm == null ? null : sjhm.trim();
    }

    public String getRylxDm() {
        return rylxDm;
    }

    public void setRylxDm(String rylxDm) {
        this.rylxDm = rylxDm == null ? null : rylxDm.trim();
    }

    public Byte getCysx() {
        return cysx;
    }

    public void setCysx(Byte cysx) {
        this.cysx = cysx;
    }

    public Byte getGlybj() {
        return glybj;
    }

    public void setGlybj(Byte glybj) {
        this.glybj = glybj;
    }

    public Byte getZbrybj() {
        return zbrybj;
    }

    public void setZbrybj(Byte zbrybj) {
        this.zbrybj = zbrybj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public Byte getCyzt() {
        return cyzt;
    }

    public void setCyzt(Byte cyzt) {
        this.cyzt = cyzt;
    }

    public Byte getDxzt() {
        return dxzt;
    }

    public void setDxzt(Byte dxzt) {
        this.dxzt = dxzt;
    }

    public Date getDxfssj() {
        return dxfssj;
    }

    public void setDxfssj(Date dxfssj) {
        this.dxfssj = dxfssj;
    }

    public String getDxcwxx() {
        return dxcwxx;
    }

    public void setDxcwxx(String dxcwxx) {
        this.dxcwxx = dxcwxx == null ? null : dxcwxx.trim();
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.cjr = cjr == null ? null : cjr.trim();
    }

    public Date getXgsj() {
        return xgsj;
    }

    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    public String getXgr() {
        return xgr;
    }

    public void setXgr(String xgr) {
        this.xgr = xgr == null ? null : xgr.trim();
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw == null ? null : zw.trim();
    }

    public Byte getZcfsbj() {
        return zcfsbj;
    }

    public void setZcfsbj(Byte zcfsbj) {
        this.zcfsbj = zcfsbj;
    }

    public Byte getZcrzjb() {
        return zcrzjb;
    }

    public void setZcrzjb(Byte zcrzjb) {
        this.zcrzjb = zcrzjb;
    }

    public Byte getQyglybj() {
        return qyglybj;
    }

    public void setQyglybj(Byte qyglybj) {
        this.qyglybj = qyglybj;
    }

    public Long getXssx() {
        return xssx;
    }

    public void setXssx(Long xssx) {
        this.xssx = xssx;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx == null ? null : zjlx.trim();
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm == null ? null : zjhm.trim();
    }

    public String getGpyId() {
        return gpyId;
    }

    public void setGpyId(String gpyId) {
        this.gpyId = gpyId == null ? null : gpyId.trim();
    }

    public Date getJhsj() {
        return jhsj;
    }

    public void setJhsj(Date jhsj) {
        this.jhsj = jhsj;
    }

    @Override
    public String toString() {
        return "YhzxxnzzcyDO{" + "id=" + id + ", xnzzId=" + xnzzId + ", bmId=" + bmId + ", nsrId=" + nsrId + ", dingid='" + dingid + '\'' + ", userid='"
                + userid + '\'' + ", xm='" + xm + '\'' + ", sjhm='" + sjhm + '\'' + ", rylxDm='" + rylxDm + '\'' + ", cysx=" + cysx + ", glybj=" + glybj
                + ", zbrybj=" + zbrybj + ", bz='" + bz + '\'' + ", cyzt=" + cyzt + ", dxzt=" + dxzt + ", dxfssj=" + dxfssj + ", dxcwxx='" + dxcwxx + '\''
                + ", cjsj=" + cjsj + ", cjr='" + cjr + '\'' + ", xgsj=" + xgsj + ", xgr='" + xgr + '\'' + ", zw='" + zw + '\'' + ", zcfsbj=" + zcfsbj
                + ", zcrzjb=" + zcrzjb + ", qyglybj=" + qyglybj + ", xssx=" + xssx + ", zjlx='" + zjlx + '\'' + ", zjhm='" + zjhm + '\'' + ", gpyId='" + gpyId
                + '\'' + ", jhsj=" + jhsj + '}';
    }
}
