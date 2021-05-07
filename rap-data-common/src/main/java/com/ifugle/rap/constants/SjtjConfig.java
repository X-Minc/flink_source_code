package com.ifugle.rap.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.aliyun.openservices.shade.com.google.common.collect.Lists;
import com.ifugle.rap.context.AppContext;
import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年08月29日 9:48
 */
public class SjtjConfig {
    private static final Properties properties = new Properties();

    private static final String TPC_XNZZ = "dsb.sjtj.tpc.xnzzid";

    // static {
    //     try {
    //         InputStream in = SjtjConfig.class.getClassLoader().getResourceAsStream("sjtj-config.properties");
    //         properties.load(in);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * 获取需要抽取中间表数据的虚拟组织ID
     * @return
     */
    public static List<Long> getTpcXnzzIds() {
        String tpcXnzzId = null;
        if (AppContext.isProductionProfile() || AppContext.isPrereleaseProfile()) {
            tpcXnzzId = properties.getProperty("prod." + TPC_XNZZ);
        } else if (AppContext.isTestProfile() || AppContext.isDevelopmentProfile()) {
            tpcXnzzId = properties.getProperty("test." + TPC_XNZZ);
        }

        if (NullUtil.isNull(tpcXnzzId)) {
            return Lists.newArrayList();
        }

        List<Long> xnzzIdList = Lists.newArrayList();
        String[] xnzzIds = tpcXnzzId.split(",");
        for (String xnzzId : xnzzIds) {
            xnzzIdList.add(Long.valueOf(xnzzId));
        }
        return xnzzIdList;
    }
}
