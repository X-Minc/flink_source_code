package com.ifugle.rap.constants;

import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年07月23日 16:48
 */
public class EsIndexConstant {
    /**
     * 环境变量名称
     */
    public static String ES_SUFFIX =
            NullUtil.isNull(System.getProperty("rap.sjtj.es.suffix")) ? "" : System.getProperty("rap.sjtj.es.suffix");
    /**
     * 钉钉非活用户访问表
     */
    public static final String USER_DD_ACTIVE_ODS = "user_dd_active_ods" + ES_SUFFIX;
    /**
     * 用户全量标签表
     */
    public static final String USER_ALL_TAG = "sync_user_all_tag" + ES_SUFFIX;
    /**
     * 企业全量标签表
     */
    public static final String COMPANY_ALL_TAG = "company_all_tag" + ES_SUFFIX;
    /**
     * 部门汇总表
     */
    public static final String DEPART_AGG_DW = "depart_agg_dw" + ES_SUFFIX;
    /**
     * 部门ODS表
     */
    public static final String DEPART_ODS = "sync_depart_ods" + ES_SUFFIX;

    /**
     * 钉钉群组表
     */
    public static final String USER_DD_GROUP_ACTIVE_ODS = "user_dd_group_active_ods" + ES_SUFFIX;

    /**
     * 钉钉群日活表
     */
    public static final String USER_DD_GROUP_DAU_ODS = "user_dd_group_dau_ods" + ES_SUFFIX;



    /**
     * 钉钉群直播表
     */
    public static final String USER_DD_GROUP_VIDEO_ODS = "user_dd_group_video_ods" + ES_SUFFIX;
}
