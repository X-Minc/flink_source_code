package com.ifugle.rap.constants;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年08月12日 16:16
 */
public interface SjtjCode {

    /**
     * limit 截取数
     */
    int PAGE_NUM = 5000;

    /**
     * 中间表 2000 截取数
     */
    int PAGE_NUM_TPC = 2000;

    /**
     * limit 钉钉接口查询条数 100（最大）
     */
    int DING_PAGE_NUM = 100;

    /**
     * 批量更新数据库数量（500时报错）
     */
    int SQL_BATCH_UPDATE_NUM = 100;

    /**
     * 批量插入数据库数量
     */
    int SQL_BATCH_INSERT_NUM = 300;

    /**
     * 企业删除状态
     */
    List<Byte> NSRZT_DM = Lists.newArrayList((byte) 7, (byte) 8, (byte) 10, (byte) 13);

    interface UserVisitStatus {
        /**
         * 全部
         */
        String ALL = "0";

        /**
         * 用户端
         */
        String YHD = "1";

        /**
         * 管理端
         */
        String GLD = "2";
    }

    interface Xxzt {
        /**
         * 信息状态 已读
         */
        Byte YD = 1;
    }

    interface Xxywlx {
        /**
         * 信息推送
         */
        String XXTS = "3";

        /**
         * 数据核实
         */
        String SJHS = "15";

        /**
         * 数据采集
         */
        String SJCJ = "16";
    }

    /**
     * 是否发送工作通知
     */
    interface Sftz {
        /**
         * 0=不发
         */
        Byte BFS = 0;

        /**
         * 默认1=发送
         */
        Byte YFS = 1;
    }

    /**
     * 信息推送信息来源
     */
    interface Xxly {
        /**
         * 默认0=未知
         */
        Byte WZ = 0;

        /**
         * 1=管理后台
         */
        Byte WEB = 1;

        /**
         * 2=微应用
         */
        Byte APP = 2;

        /**
         * 9=开放接口
         */
        Byte TPC = 9;
    }

    interface Xxhszt {
        /**
         * 未核实
         */
        Byte WHS = 0;

        /**
         * 已核实
         */
        Byte YHS = 1;
    }

    interface Qlx {
        /**
         * 税企交流群
         */
        Byte SQJLQ = 2;

        /**
         * 在线咨询群
         */
        Byte ZXZXQ = 3;
    }

    /**
     * 跨组织税务机关本级虚拟组织数据是否显示标志
     */
    interface Bjxsbz {
        /**
         * 不显示
         */
        Byte NO = 0;
        /**
         * 显示
         */
        Byte YES = 1;
    }

    /**
     * 发送标记相关
     */
    interface Fsbj {
        /**
         * 未审核
         */
        Byte WSH = 1;
        /**
         * 未发送
         */
        Byte WFS = 2;

        /**
         * 已扫描（扫描交给推送任务）
         */
        Byte YSM = 22;
        /**
         * 已扫描_人员不存在（扫描交给推送任务，但是没有对应人员）
         */
        Byte YSM_NOBODY = 21;

        /**
         * 已推送（推送给钉钉）
         */
        Byte YTS = 11;
        /**
         * 推送失败（推送给钉钉失败）
         */
        Byte TSSB = 12;

        /**
         * 发送成功（同步结果，显示成功）
         */
        Byte FSCG = 3;
        /**
         * 发送失败（同步结果，显示失败）
         */
        Byte FSSB = 4;
        /**
         * 无效用户（同步结果，限流错误）
         */
        Byte WXYH = 5;
        /**
         * 限流用户（同步结果，限流错误）
         */
        Byte XLYH = 6;

    }
}
