package com.ifugle.rap.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:23
 */
public interface DsbCode {

    /**
     * 公共微应用的常量
     */
    public interface AppCode {
        /**
         * 微应用名字
         */
        public interface AppName {
            /**
             * 涉税通知
             */
            String SSTZ = "涉税通知";
            /**
             * 政策法规
             */
            String ZCFG = "政策法规";
            /**
             * 办税指南
             */
            String BSZN = "办税指南";
            /**
             * 发票真伪
             */
            String FPZW = "发票真伪";
            /**
             * 热点问题
             */
            String RDWT = "热点问题";
            /**
             * 在线值班
             */
            String ZXZB = "在线值班";
            /**
             * 涉税查询
             */
            String SSCX = "涉税查询";
            /**
             * 个税查询
             */
            String GSCX = "个税查询";
            /**
             * 网上办税
             */
            String WSBS = "网上办税";
            /**
             * 公文处理
             */
            String GWCL = "公文处理";
            /**
             * 办税日历
             */
            String BSRL = "办税日历";
            /**
             * 办税网点
             */
            String BSWD = "办税网点";
            /**
             * 留言板
             */
            String LYB = "留言板";
        }

        /**
         * 微应用Id
         */
        public interface AppId {
            /**
             * 涉税通知
             */
            long SSTZ = 1;
            /**
             * 政策法规
             */
            long ZCFG = 2;
            /**
             * 办税指南
             */
            long BSZN = 3;
            /**
             * 发票真伪
             */
            long FPZW = 4;
            /**
             * 热点问题
             */
            long RDWT = 5;
            /**
             * 在线值班
             */
            long ZXZB = 6;
            /**
             * 涉税查询
             */
            long SSCX = 7;
            /**
             * 个税查询
             */
            long GSCX = 8;
            /**
             * 网上办税
             */
            long WSBS = 9;
            /**
             * 公文处理
             */
            long GWCL = 10;
            /**
             * 办税日历
             */
            long BSRL = 11;
            /**
             * 办税网点
             */
            long BSWD = 12;
            /**
             * 留言板
             */
            long LYB = 13;

            /**
             * 金三专栏
             */
            long JSZL = 14;

            /**
             * 业界资讯
             */
            long YJZX = 15;

            /**
             * 移动办税
             */
            long YDBS = 16;

            /**
             * 在线调查
             */
            long ZXDC = 17;

            /**
             * 好会计
             */
            long HKJ = 18;

            /**
             * 数据填报
             */
            long SJTB = 19;

        }

        /**
         * 微应用类型
         */
        public interface AppType {
            /**
             * 1：微应用
             */
            Byte WYY = 1;
            /**
             * 2：模块
             */
            Byte GNMK = 2;
            /**
             * 3：混合
             */
            Byte HH = 3;
            /**
             * 4:模块外链
             */
            Byte WLMK = 4;
            /**
             * 5:开发中
             */
            Byte KFZ = 5;

            /**
             * 31:第三方 未审核（草稿）
             */
            Byte DSF_WSH = 31;
            /**
             * 32:第三方 审核中
             */
            Byte DSF_SHZ = 32;
            /**
             * 33:第三方 审核未通过
             */
            Byte DSF_SHWTG = 33;
            /**
             * 34:第三方 审核通过（未上架）
             */
            Byte DSF_WSJ = 34;
            /**
             * 35:第三方 审核通过（已上架）
             */
            Byte DSF_YSJ = 35;
        }

        /**
         * 微应用类型
         */
        public interface AppGxzt {
            /**
             * 更新状态，无新挂新
             */
            public byte WGX = 0;
            /**
             * 更新状态，有新挂新
             */
            public byte YGX = 1;
        }

        /**
         * 微应用启用标记
         */
        public interface AppQybj {
            /**
             * 启用标记，未启用
             */
            byte WQY = 0;
            /**
             * 启用标记，已启用
             */
            byte YQY = 1;
        }

        /**
         * 虚拟组织类型
         */
        public interface XNZZLX {
            /**
             * 县市级标准化虚拟组织1
             */
            Byte XSJ = 1;
            /**
             * 地市级标准化虚拟组织2
             */
            Byte DSJ = 2;
            /**
             * 省级标准化虚拟组织3
             */
            Byte SJ = 3;
        }

        /**
         * 群模板类型
         */
        public interface QMBLX {
            /**
             * 群模板类型_自动创建群
             */
            Byte ZDCJQ = 1;
            /**
             * 群模板类型_税企交流群
             */
            Byte SQJLQ = 2;
            /**
             * 群模板类型_在线咨询群
             */
            Byte XXZXQ = 3;
        }
    }

    /**
     * 成员属性
     */
    public interface Cysx {
        /**
         * 税务局人员
         */
        Byte CYSX_SWJRY = 1;
        /**
         * 企业人员
         */
        Byte CYSX_QYRY = 2;
        /**
         * 12366服务人员
         */
        Byte CYSX_12366RY = 3;
        /**
         * 其他第三方服务人员
         */
        Byte CYSX_DSFWFRY = 4;

        /**
         * 自助注册人员
         */
        Byte CYSX_ZZZCRY = 5;

        /**
         * 自然人员
         */
        Byte CYSX_ZRRY = 6;
    }

    /**
     * 部门属性
     */
    public interface Bmsx {
        /**
         * 根部门
         */
        Byte BMSX_ROOT = 0;
        /**
         * 税务局人员
         */
        Byte BMSX_SWJBM = 1;
        /**
         * 企业人员
         */
        Byte BMSX_QYBM = 2;
        /**
         * 12366服务人员
         */
        Byte BMSX_12366BM = 3;
        /**
         * 其他第三方服务人员
         */
        Byte BMSX_DSFWFBM = 4;
        /**
         * 自助注册部门
         */
        Byte BMSX_ZZZCBM = 5;
        /**
         * 自助注册部门
         */
        Byte BMSX_ZRRBM = 6;
    }

    /**
     * 人员类型代码
     */
    public interface Rylxdm {
        /**
         * 法定代表人
         */
        String FDDBR = "1";
        /**
         * 财务负责人
         */
        String CWFZR = "2";
        /**
         * 办税人
         */
        String BSR = "3";
        /**
         * 其他税务人员
         */
        String QTSSRY = "4";
        /**
         * 购票员
         */
        String GBY = "5";
    }

    /**
     * 人员类型
     */
    public interface Rylx {
        /**
         * 法定代表人
         */
        String FDDBR = "法定代表人";
        /**
         * 财务负责人
         */
        String CWFZR = "财务负责人";
        /**
         * 办税人
         */
        String BSR = "办税人";
        /**
         * 其他税务人员
         */
        String QTSSRY = "其他办税人";
        /**
         * 购票员
         */
        String GBY = "购票员";
    }

    /**
     * 所属机构类型代码
     */
    public interface SSJGLXDM {
        /**
         * 所属机构类型代码 ：国税
         */
        String GS = "1";
        /**
         * 所属机构类型代码 ：地税
         */
        String DS = "2";
        /**
         * 所属机构类型代码 ：国地税
         */
        String GDS = "3";

        Byte GS_BYTE = 1;

        Byte DS_BYTE = 2;

        Byte GDS_BYTE = 3;
    }

    /**
     * 虚拟组织类型
     */
    public interface XNZZLX {
        /**
         * 地市级标准化虚拟组织 2
         */
        public static final int DSJBZHXNZZ = 2;
        /**
         * 省级标准化虚拟组织3
         */
        public static final int SJXNZZ = 3;
        /**
         * 县市级标准化虚拟组织1
         */
        public static final int XSJXNZZ = 1;
    }

    /**
     * 纳税人状态代码
     */
    public interface NsrztDm {
        /**
         * 纳税人状态代码:正常
         */
        String NSRZTDM_ZC = "03";
        /**
         * 纳税人状态代码：删除
         */
        String NSRZTDM_SC = "07";

        // CHS:简体中文
        String NSRZTDM_ZC_CHS = "正常";

        String NSRZTDM_SC_CHS = "删除";
    }

    /**
     * 自助注册标记
     */
    public interface ZzzcBj {
        /**
         * 自助注册
         */
        byte ZZZCBJ_ZZZC = 5;
        /**
         * 清册导入
         */
        byte ZZZCBJ_QCDR = 3;
    }

    /**
     * 虚拟组织自助注册开关标记
     */
    public interface ZxzzOpenFlag {

        /**
         * 完全关闭
         */
        Byte CLOSED = 0;
        /**
         * 部分开放
         */
        Byte OPEN_SOME = 1;
        /**
         * 全部开放
         */
        Byte OPEN_ALL = 2;
    }

    /**
     * 虚拟组织管理员标记
     */
    public interface DsbAdminFlag {
        /**
         * 管理员
         */
        String DSBGLY = "1";
        /**
         * 普通成员
         */
        String COMMON_USER = "0";
        /**
         * 管理员
         */
        Byte DSBGLY_BYTE = 1;
        /**
         * 普通成员
         */
        Byte COMMON_USER_BYTE = 0;

    }

    /**
     * 重点税源标记
     */
    public interface ZdsyFlag {
        /**
         * 重点
         */
        Byte ZDSY = 1;
        /**
         * 非重点
         */
        Byte FZDSY = 0;

        /**
         * 重点
         */
        String ZDSYSTR = "是";
        /**
         * 非重点
         */
        String FZDSYSTR = "否";
    }

    interface BjStr {
        String BJ_Y = "是";
        String BJ_N = "否";
    }

    interface BjByte {
        Byte BJ_Y = 1;
        Byte BJ_N = 0;
    }

    public interface Zbrybj {
        /**
         * 值班人员
         */
        Byte ZBRY = 1;
        /**
         * 非值班人员
         */
        Byte NON_ZBRY = 0;
    }

    interface Cyzt {
        /**
         * 激活
         */
        Byte ACTIVE = 1;
        /**
         * 未激活
         */
        Byte UNACTIVE = 0;
        /**
         * 删除
         */
        Byte DELETE = 2;
    }

    /**
     * 激活标记
     */
    interface Jhbj {
        /**
         * 已激活
         */
        Byte YJH = 1;
        /**
         * 未激活
         */
        Byte WJH = 0;

    }

    /**
     * 成员短信状态
     */
    interface Dxzt {
        /**
         * 正常
         */
        Byte ZC = 1;
        /**
         * 不正常
         */
        Byte BZC = 0;
    }

    /**
     * 管理员标记
     */
    interface Glybj {
        /**
         * 管理员
         */
        Byte Y = 1;
        /**
         * 非管理员
         */
        Byte N = 0;

    }

    /**
     * 税务机关标志
     */
    interface Swjgbz {
        /**
         * 税务机关
         */
        Byte SWJG = 0;
        /**
         * 部门
         */
        Byte BM = 1;

    }

    /**
     * 行业级别
     */
    interface Hyjb {
        /**
         * 门类
         */
        String ML="ML";
        /**
         * 大类
         */
        String DL="DL";
        /**
         * 中类
         */
        String ZL="ZL";
        /**
         * 小类
         */
        String XL="XL";
    }

    static Set<Byte> getAllBmsx() {
        Set<Byte> bmsxs = new HashSet<>();
        bmsxs.add(Bmsx.BMSX_ROOT);
        bmsxs.add(Bmsx.BMSX_SWJBM);
        bmsxs.add(Bmsx.BMSX_QYBM);
        bmsxs.add(Bmsx.BMSX_12366BM);
        bmsxs.add(Bmsx.BMSX_DSFWFBM);
        return bmsxs;
    }

    /**
     * 成员同步钉钉标记
     */
    interface IsSend{
        /**
         * 未同步
         */
        byte NOT = 0;
        /**
         * 已同步
         */
        byte YES = 1;
    }

    interface Tbzt {
        /**
         * 未同步
         */
        Byte NOT = 0;
        /**
         * 已同步
         */
        Byte YES = 1;
    }
}
