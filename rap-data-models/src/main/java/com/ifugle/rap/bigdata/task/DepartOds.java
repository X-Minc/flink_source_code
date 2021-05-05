package com.ifugle.rap.bigdata.task;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2019年07月26日 9:37
 */
@Data
public class DepartOds implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @SerializedName("id")
    private Long id;

    /**
     * 虚拟组织ID
     */
    @SerializedName("xnzz_id")
    private Long xnzzId;

    /**
     * 上级部门ID
     */
    @SerializedName("parent_id")
    private Long parentId;

    /**
     * 所属税务机关代码（本级无税务机关，取上级税务机关代码）
     */
    @SerializedName("ssswjg_dm")
    private String ssswjgDm;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("cjsj")
    private Date cjsj;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("xgsj")
    private Date xgsj;

    /**
     * 部门属性：1、政府机构2、纳税人4、服务机构
     */
    @SerializedName("bmsx")
    private Byte bmsx;

    /**
     * 删除标记
     */
    @SerializedName("is_delete")
    private Boolean isDelete;

    /**
     * 在父部门次序值
     */
    @SerializedName("xssx")
    private Long xssx;

    /**
     * 部门名称
     */
    @SerializedName("bmmc")
    private String bmmc;

    /**
     * 部门全路径
     */
    @SerializedName("bmmcPath")
    private String bmmcPath;

    /**
     * 部门路径
     */
    @SerializedName("bm_ids")
    private List<Long> bmIds;
}
