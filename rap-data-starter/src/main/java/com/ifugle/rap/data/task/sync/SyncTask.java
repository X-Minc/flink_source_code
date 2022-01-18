package com.ifugle.rap.data.task.sync;

import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.SpecialFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.sqltransform.specialfiledextractor.SingleAggregationSpecialFiledExtractor;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 使用说明
 * <p>
 * 当前提供字段提取器正对单字段count。
 * 比如select a,b,count(c) from d group by a,b；
 * 如果要分day和30day以及月，请务必在where后加{time_condition}
 * 比如select a,b,count(c) from d  where {time_condition} group by a,b；
 * count目前只支持count({filed})，{filed}为字段名称，不能为1或者*；
 * 若要实现多字段count请自定义实现
 * 特殊字段提取器{@link SpecialFiledExtractorBase}
 * 公共字段设置器{@link CommonFiledExtractorBase}；
 * 时间格式过滤只支持 yyyy-MM-dd HH:mm:ss格式
 * limit 为hits部分查询数据的大小
 * glimit为分组后显示bucket的数量
 * 若要自定义规则sql转为dsl可以重写{@link TransformBase}
 *
 * @author Minc
 * @date 2021/12/30 14:20
 */
@Component
public class SyncTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTask.class);

    @Autowired
    SyncFactory syncFactory;

    @Scheduled(cron = "0 1 0 * * ?")
    public void getQuery() {
        try {
            syncFactory.addTransforms(
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule()
            );
            syncFactory.addSqlTasks(
                    xxIndex40055_1_3(),
                    xxIndex40055_1_2(),
                    xxIndex40055_1_1(),
                    xxIndex40054_1_3(),
                    xxIndex40054_1_2(),
                    xxIndex40054_1_1(),
                    xxIndex40066_1(),
                    xxIndex40062_1(),
                    xxIndex40055_1(),
                    xxIndex40054_1(),
                    xxIndex40053_1(),
                    xxIndex40052_1(),
                    nsrIndex20020_1(),
                    nsrIndex20020_5(),
                    nsrIndex20021_1(),
                    nsrIndex20021_5(),
                    nsrIndex10030_5(),
                    nsrIndex10030_1(),
                    nsrIndex10010_5(),
                    nsrIndex10010_1(),
                    nsrIndex10020_1(),
                    nsrIndex10020_5(),
                    nsrIndex20060_1(),
                    nsrIndex20060_5(),
                    nsrIndex20070_1(),
                    nsrIndex20070_5(),
                    nsrIndex20040_1(),
                    nsrIndex20040_5(),
                    nsrIndex20010_5(),
                    nsrIndex20010_1(),
                    qzIndex40049(),
                    qzIndex40044(),
                    qzIndex40043(),
                    qzIndex40042(),
                    qzIndex40040()
            );
            syncFactory.runAllTask();
        } catch (Exception e) {
            LOGGER.error("产生错误！", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initDaily() {
        try {
            syncFactory.init();
        } catch (Exception e) {
            LOGGER.error("初始化时发生错误！" + e);
        }
    }

    // --------------------------------------------------------------------------------------------
    //   get sql tasks
    // --------------------------------------------------------------------------------------------

    /**
     * 消息—40055-1-3 消息推送已读其他数
     */
    private List<SqlTask> xxIndex40055_1_3() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) ANDdjzclx_flag =\"3\" {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40055")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("3");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40055-1-2 消息推送已读个体数
     */
    private List<SqlTask> xxIndex40055_1_2() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) ANDdjzclx_flag =\"2\" {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40055")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("2");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40055-1-1 消息推送已读企业数
     */
    private List<SqlTask> xxIndex40055_1_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40055")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1-3 消息推送通知其他数
     */
    private List<SqlTask> xxIndex40054_1_3() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"3\"  {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40054")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("3");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1-2 消息推送通知个体数
     */
    private List<SqlTask> xxIndex40054_1_2() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"2\"  {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40054")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("2");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1-1 消息推送通知企业数
     */
    private List<SqlTask> xxIndex40054_1_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"1\" GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx_flag =\"1\" GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40054")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1")
                .setDim3("1011")
                .setDimData3("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40066-1 月消息通知公告已读企业数
     */
    private List<SqlTask> xxIndex40066_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40062")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40062-1 月推送通知公告企业数
     */
    private List<SqlTask> xxIndex40062_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40062")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40055-1 涉税通知已读纳税人数
     */
    private List<SqlTask> xxIndex40055_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40055")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1 涉税通知推送纳税人数
     */
    private List<SqlTask> xxIndex40054_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40054")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40053-1 涉税通知接口推送次数
     */
    private List<SqlTask> xxIndex40053_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND xxly = 9 AND and czzt = 6  and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND xxly = 9 AND and czzt = 6  and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40053")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40052-1 涉税通知推送次数
     */
    private List<SqlTask> xxIndex40052_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND and czzt = 6 and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND and czzt = 6 and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40052")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10030-5 企业总人数
     */
    private List<SqlTask> nsrIndex10030_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_qy  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=2 GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_qy  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=2 GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10030")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—10030-1 企业激活人数
     */
    private List<SqlTask> nsrIndex10030_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_qy  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=2 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_qy  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=2 {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10030")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—10010-5 人员总数
     */
    private List<SqlTask> nsrIndex10010_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_all  WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_all  WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—10010-1 激活人数
     */
    private List<SqlTask> nsrIndex10010_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(DISTINCT yh_id) FROM bigdata_yhzx_xnzz_yh_all  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(DISTINCT yh_id) FROM bigdata_yhzx_xnzz_yh_all  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2)  {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—10020-1 机构激活人数
     */
    private List<SqlTask> nsrIndex10020_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_jg  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_jg  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—10020-5 机构总人数
     */
    private List<SqlTask> nsrIndex10020_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_jg  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh_jg  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }//

    /**
     * 纳税人—20060-1 纳税人未登记注册激活数
     */
    private List<SqlTask> nsrIndex20060_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20060")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20060-5 纳税人未登记注册总数
     */
    private List<SqlTask> nsrIndex20060_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"3\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20060")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20040-1 纳税人个体激活数
     */
    private List<SqlTask> nsrIndex20040_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20040")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20040-5 纳税人个体总数
     */
    private List<SqlTask> nsrIndex20040_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"2\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20040")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20070-1 纳税人企业激活数
     */
    private List<SqlTask> nsrIndex20070_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20070")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20070-5 纳税人企业总数
     */
    private List<SqlTask> nsrIndex20070_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_flag =\"1\" {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20070")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20021-1 小规模纳税人激活企业数
     */
    private List<SqlTask> nsrIndex20021_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=2 AND jhbj=1 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=2 AND jhbj=1 {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20021")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20021-5 小规模纳税人总企业数
     */
    private List<SqlTask> nsrIndex20021_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=2 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=2 {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20021")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20020-1 一般纳税人激活企业数
     */
    private List<SqlTask> nsrIndex20020_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=1 AND jhbj=1 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=1 AND jhbj=1 {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20020-5 一般纳税人总企业数
     */
    private List<SqlTask> nsrIndex20020_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=1 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND nsrlx_flag=1 {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20010-1 激活纳税人数
     */
    private List<SqlTask> nsrIndex20010_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20010-5 纳税人总数
     */
    private List<SqlTask> nsrIndex20010_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 群组—40040  群组钉钉群总数
     */
    private List<SqlTask> qzIndex40040() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select swjg_dms,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40040")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }//

    /**
     * 群组—40042 群组群成员数量
     */
    private List<SqlTask> qzIndex40042() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select swjg_dms,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40042")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }

    /**
     * 群组—40043 群组纳税人数（户）
     */
    private List<SqlTask> qzIndex40043() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy_nsr  where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select swjg_dms,count(nsr_id) from bigdata_yhzx_qz_cy_nsr  where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40043")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }

    /**
     * 群组—40044 群组一般纳税人数（户）
     */
    private List<SqlTask> qzIndex40044() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy_nsr  where qlx=2 and is_deleted=0 and swjgbz in (1,2) and zzsnslx=1 {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select swjg_dms,count(nsr_id) from bigdata_yhzx_qz_cy_nsr  where qlx=2 and is_deleted=0 and swjgbz in (1,2) and zzsnslx=1 {time_condition} group by swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40044")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }

    /**
     * 群组—40049群组平台群总数
     */
    private List<SqlTask> qzIndex40049() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(qz_mb_id) from bigdata_yhzx_qz_mb where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select swjg_dms,count(qz_mb_id) from bigdata_yhzx_qz_mb where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40049")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return sqlToSqlTask(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }//

    // --------------------------------------------------------------------------------------------
    //   method to get sql tasks
    // --------------------------------------------------------------------------------------------

    /**
     * 将sql转为sqlTask
     *
     * @param selfSql                     本级sql
     * @param superiorSql                 上级sql
     * @param commonFiledExtractorBuilder 公共字段提取
     * @param isQz                        是否为群组指标
     * @throws Exception 异常
     */
    private List<SqlTask> sqlToSqlTask(String selfSql, String superiorSql, CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder, Boolean isQz, String timeFiled) throws Exception {
        List<SingleAggregationSpecialFiledExtractor> specialSelfAgg = getSpecialSelfAgg(isQz);
        List<SingleAggregationSpecialFiledExtractor> specialSuperiorAgg = getSpecialSuperiorAgg(isQz);
        List<CommonFiledExtractor> commonAgg = getCommonAgg(commonFiledExtractorBuilder, isQz);
        List<SqlTask> selfSqlTasks =
                SqlTransformDslUtil.doPreTransform(selfSql, true, !isQz, true, specialSelfAgg, commonAgg, timeFiled, true);
        List<SqlTask> superiorSqlTasks =
                SqlTransformDslUtil.doPreTransform(superiorSql, true, !isQz, true, specialSuperiorAgg, commonAgg, timeFiled, false);
        selfSqlTasks.addAll(superiorSqlTasks);
        return selfSqlTasks;
    }

    /**
     * 获得公共的字段提取器集合
     *
     * @param commonFiledExtractorBuilder 公共字段提取器
     * @param isQz                        是否为群组
     * @return 包含当天，30天，月的字段提取器集合
     * @throws CloneNotSupportedException 异常
     */
    private List<CommonFiledExtractor> getCommonAgg(CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder, Boolean isQz) throws CloneNotSupportedException {
        List<CommonFiledExtractor> commonFiledExtractorList = new ArrayList<>();
        commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
        if (!isQz)
            commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
        commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(true).build());
        return commonFiledExtractorList;
    }

    /**
     * 获得特殊的本级字段提取器集合
     *
     * @param isQz 是否为群组
     * @return 特殊的本级字段提取器集合（包含，当天，30天，当月）
     * @throws Exception 异常
     */
    private List<SingleAggregationSpecialFiledExtractor> getSpecialSelfAgg(Boolean isQz) throws Exception {
        SingleAggregationSpecialFiledExtractor singleAggregationSpecialFiledExtractor = new SingleAggregationSpecialFiledExtractor(new SingleAggregationSpecialFiledExtractor.DealWithTotalAndSelf());
        SingleAggregationSpecialFiledExtractor singleAggregationSpecialFiledExtractorInc = new SingleAggregationSpecialFiledExtractor(new SingleAggregationSpecialFiledExtractor.DealWithIncAndSelf());
        List<SingleAggregationSpecialFiledExtractor> singleAggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractor);
        if (!isQz)
            singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractorInc);
        singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractorInc);
        return singleAggregationSpecialFiledExtractorArrayList;
    }

    /**
     * 获得特殊的上级字段提取器
     *
     * @param isQz 是否为群组
     * @return 特殊的上级字段提取器集合（包含，当天，30天，当月）
     */
    private List<SingleAggregationSpecialFiledExtractor> getSpecialSuperiorAgg(Boolean isQz) throws Exception {
        SingleAggregationSpecialFiledExtractor singleAggregationSpecialFiledExtractor = new SingleAggregationSpecialFiledExtractor(new SingleAggregationSpecialFiledExtractor.DealWithTotalAndSuperior());
        SingleAggregationSpecialFiledExtractor singleAggregationSpecialFiledExtractorInc = new SingleAggregationSpecialFiledExtractor(new SingleAggregationSpecialFiledExtractor.DealWithIncAndSuperior());
        List<SingleAggregationSpecialFiledExtractor> singleAggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractor);
        if (!isQz)
            singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractorInc);
        singleAggregationSpecialFiledExtractorArrayList.add(singleAggregationSpecialFiledExtractorInc);
        return singleAggregationSpecialFiledExtractorArrayList;
    }
}
