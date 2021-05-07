package com.ifugle.rap.bigdata.task.util;

import com.ifugle.rap.bigdata.task.CompanyAllTag;
import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.UserAllTag;
import com.ifugle.rap.utils.UserOds;
import com.ifugle.util.NullUtil;
import com.ifugle.util.SHA1Util;

/**
 * @author XuWeigang
 * @since 2019年07月24日 11:32
 */
public class EsKeyUtil {


    /**
     * 获取DepartOds的key值
     *
     * @param depart
     *
     * @return
     */
    public static String getDepartOdsKey(DepartOds depart) {
        StringBuilder keyId = new StringBuilder();
        keyId.append(depart.getId());
        return SHA1Util.base64Digest(keyId.toString(), "utf-8");
    }

    /**
     * 返回15位的中间表yhId，1开头为机构人员， 2开头为企业人员
     *
     * @param id
     * @param cysx
     *
     * @return
     */
    public static Long getTpcYhId(Long id, Byte cysx) {
        String prefix = String.valueOf(cysx);
        StringBuilder tpcYhId = new StringBuilder();
        tpcYhId.append(prefix);
        String yhId = String.valueOf(id);
        if (yhId.length() >= 14) {
            // 大于等于14位时直接补第一位前缀
            tpcYhId.append(yhId);
            return Long.valueOf(tpcYhId.toString());
        }

        // 不满14位时中间补0
        int fillNum = 14 - yhId.length();
        for (int i = 0; i < fillNum; i++) {
            tpcYhId.append("0");
        }
        tpcYhId.append(yhId);
        return Long.valueOf(tpcYhId.toString());
    }

    /**
     * 获取UserOds的key值
     *
     * @param user
     *
     * @return
     */
    public static String getUserOdsKey(UserOds user) {
        StringBuilder keyId = new StringBuilder();
        keyId.append(user.getXnzzId())
                .append("_").append(user.getYhType());
        keyId.append("_");
        keyId.append(NullUtil.isNotNull(user.getYhNsrId()) ? user.getYhNsrId() : "");
        keyId.append("_");
        keyId.append(NullUtil.isNotNull(user.getNsrId()) ? user.getNsrId() : "");
        return SHA1Util.base64Digest(keyId.toString(), "utf-8");
    }

    /**
     * 获取UserAllTag的key值
     *
     * @param allTag
     *
     * @return
     */
    public static String getUserAllTagKey(UserAllTag allTag) {
        StringBuilder keyId = new StringBuilder();
        keyId.append(allTag.getXnzzId())
                .append("_").append(allTag.getYhType());
        keyId.append("_");
        keyId.append(NullUtil.isNotNull(allTag.getYhNsrId()) ? allTag.getYhNsrId() : "");
        keyId.append("_");
        keyId.append(NullUtil.isNotNull(allTag.getNsrId()) ? allTag.getNsrId() : "");
        return SHA1Util.base64Digest(keyId.toString(), "utf-8");
    }

    public static String getCompanyOdsKey(CompanyOds company) {
        StringBuilder keyId = new StringBuilder();
        keyId.append(company.getXnzzId())
                .append("_").append(company.getNsrId());
        return SHA1Util.base64Digest(keyId.toString(), "utf-8");
    }

    /**
     * 获取CompanyAllTag的key值
     *
     * @param company
     *
     * @return
     */
    public static String getCompanyAllTagKey(CompanyAllTag company) {
        StringBuilder keyId = new StringBuilder();
        keyId.append(company.getXnzzId())
                .append("_").append(company.getNsrId());
        return SHA1Util.base64Digest(keyId.toString(), "utf-8");
    }

}
