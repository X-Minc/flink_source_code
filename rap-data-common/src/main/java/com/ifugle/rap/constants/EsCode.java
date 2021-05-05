package com.ifugle.rap.constants;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ifugle.util.NullUtil;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月23日 14:45
 */
public interface EsCode {
    /**
     * es 日志输出控制
     */
    AtomicBoolean LOG_OUT = new AtomicBoolean(false);

    /**
     * dd线程池并发数
     */
    Integer DD_POOL_SIZE =
            NullUtil.isNull(System.getProperty("rap.sjtj.dd.es.pool.size")) ? 5 : Integer.valueOf(System.getProperty("rap.sjtj.dd.es.pool.size"));

    /**
     * es 插入条数限制
     */
    int ES_PAGE_NUM = 5000;

    /**
     * es 异步查询条数限制
     */
    int ES_ASYN_FIND_PAGE_NUM = 20000;

    /**
     * 子文档ES更新条数限制
     */
    int ES_PAGE_NUM_BY_PARENT = 2500;

    /**
     * es 查询条数限制
     */
    int ES_FIND_PAGE_NUM = 5000;

    /**
     * es 查询参数个数限制
     */
    int ES_PARAM_NUM = 1000;

    /**
     * 提交ES Service方法处理的批量条数
     */
    int ES_BATCH_NUM = 50000;

    /**
     * 税务机关全国总税务机关代码
     */
    String ROOT_SWJG = "00000000000";

    /**
     * 机构人员在企业用户主从表中的虚拟父级ID
     */
    String JGRY_PARENT_COMPANY_ID = "1000000000";

    /**
     * 是
     */
    Byte YES = 1;

    /**
     * 否
     */
    Byte NO = 0;

    /**
     * 默认值0
     */
    BigDecimal ZREO = BigDecimal.ZERO;

    /**
     * 单批处理的最大部门数
     */
    int BM_BATCH_NUM = 30;

    /**
     * 去重精度阈值
     */
    int PRECISION_THRESHOLD = 40000;

    /**
     *
     */

    interface EsTagType {
        /**
         * 0=不变
         */
        Byte INVARIANT = 0;

        /**
         * 1=新增
         */
        Byte ADD = 1;

        /**
         * 2=减少
         */
        Byte DELETE = 2;
    }

    interface EsBgTagType {
        /**
         * 1=更新
         */
        Byte UPDATE = 1;

        /**
         * 2=删除
         */
        Byte DELETE = 2;
    }

    interface EsAllTagType {
        /**
         * 0=未激活
         */
        Byte WJH = 0;

        /**
         * 1=激活
         */
        Byte YJH = 1;
    }

    interface EsActiveTagType {
        /**
         * 1=日活跃
         */
        Byte HY_DAY = 1;

        /**
         * 2=近30天活跃
         */
        Byte HY_THIRTY = 2;

        /**
         * 3=月活跃
         */
        Byte HY_MONTH = 3;
    }

    interface EsAddTagType {
        /**
         * 0=不是新增用户
         */
        Byte UPDATE = 0;

        /**
         * 2=新增用户
         */
        Byte ADD = 1;

        /**
         * 2=没有非活用户
         */
        Byte ALL = 2;
    }

    interface EsYhType {
        /**
         * 用户
         */
        Byte USER = 1;
        /**
         * 第三方人员
         */
        Byte TPC = 0;
    }

    interface EsTagStatus {
        /**
         * 1=激活
         */
        Byte JH = 1;

        /**
         * 2=丁税宝活跃
         */
        Byte HY = 2;

        /**
         * 3=认证
         */
        Byte RZ = 3;

        /**
         * 4=同步
         */
        Byte TB = 4;

        /**
         * 5=在册
         */
        Byte ZC = 5;

        /**
         * 6=钉钉活跃
         */
        Byte DD_HY = 6;

        /**
         * 7=30日丁税宝活跃
         */
        Byte DAY30_HY = 7;

        /**
         * 8=30日钉钉活跃
         */
        Byte DAY30_DD_HY = 8;

        /**
         * 9=周累计丁税宝活跃
         */
        Byte WEEK_HY = 9;

        /**
         * 10=周累计钉钉活跃
         */
        Byte WEEK_DD_HY = 10;

        /**
         * 11=月累计丁税宝活跃
         */
        Byte MONTH_HY = 11;

        /**
         * 12=月累计钉钉活跃
         */
        Byte MONTH_DD_HY = 12;
    }

    /**
     * 人员类型
     */
    interface EsRylx {
        /**
         * 法定代表人
         */
        Byte FDDBR = 1;

        /**
         * 财务负责人
         */
        Byte CWFZR = 2;

        /**
         * 办税人
         */
        Byte BSR = 3;

        /**
         * 其他办税人
         */
        Byte QTBSR = 4;

        /**
         * 购票员
         */
        Byte GPY = 5;
    }

    /**
     * 活跃类型标签
     */
    interface EsHyType {
        /**
         * 1：当日导入-激活-0活跃人数：当日导入-当日激活-但未登入应用的人数
         */
        Byte DRJH_WHY = 1;

        /**
         * 2：当日导入-激活-活跃人数：当日导入-当日激活-且当日登入应用的人数
         */
        Byte DRJH_HY = 2;

        /**
         * 3：历史导入-历史激活-活跃人数：历史导入-历史激活-且当日登入应用的人数
         */
        Byte LS_DRJH_HY = 3;

        /**
         * 4：历史导入-当日激活-活跃人数：历史导入-当日激活-且当日登入应用的人数
         */
        Byte LS_DR_JH_HY = 4;

        /**
         * 5：历史导入-当日激活-0活跃人数：历史导入-当日激活-但未登入应用的人数
         */
        Byte LS_DR_JH_WHY = 5;
    }

    /**
     * 群组成员删除状态
     */
    interface IsDeleted {
        /**
         * 不删除
         */
        Byte NO = 0;

        /**
         * 删除
         */
        Byte YES = 1;
    }
}
