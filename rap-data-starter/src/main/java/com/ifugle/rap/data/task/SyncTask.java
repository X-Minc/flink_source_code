package com.ifugle.rap.data.task;

import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.sqltransform.specialfiledextractor.AggregationSpecialFiledExtractor;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 请严格按照空格分隔
 * 时间格式过滤只支持 yyyy-MM-dd HH:mm:ss格式
 * limit 为hits部分查询数据的大小
 * glimit为分组后显示bucket的数量
 * count()请加字段，且务必不需要空格
 * 如果要分day和30day以及月，请务必在where后加{time_condition}
 *
 * @author Minc
 * @date 2021/12/30 14:20
 */
@Component
public class SyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTask.class);

    @Autowired
    SyncFactory syncFactory;

    @Scheduled(fixedDelay = 1000L * 60 * 30)
    public void getQuery() {
        try {
            syncFactory.addTransforms(
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule()
            );
            syncFactory.addSqlTasks(
                    xxIndex40055_1_2(),
                    xxIndex40055_1_1(),
                    xxIndex40054_1_2(),
                    xxIndex40054_1_1(),
                    xxIndex40066_1(),
                    xxIndex40062_1(),
                    xxIndex40055_1(),
                    xxIndex40054_1(),
//                    xxIndex40053_1(),
                    xxIndex40052_1(),
                    nsrIndex10030_5(),
                    nsrIndex10030_1(),
                    nsrIndex10010_5(),
                    nsrIndex10010_1(),
                    nsrIndex10020_1(),
                    nsrIndex10020_5(),
                    nsrIndex20070_1(),
                    nsrIndex20070_5(),
                    nsrIndex20040_1(),
                    nsrIndex20040_5(),
                    nsrIndex20010_5(),
                    nsrIndex20010_1(),
                    qzIndex40042(),
                    qzIndex40043(),
                    qzIndex40040()
            );
            syncFactory.runAllTask();
        } catch (Exception e) {
            LOGGER.error("产生错误！", e);
        }
    }

    /************************************************************************************************************************************************************************************************************************
     ************************************************************************************************* get SqlTask***********************************************************************************************************
     ************************************************************************************************************************************************************************************************************************/

    /**
     * 消息—40055-1-2
     */
    private List<SqlTask> xxIndex40055_1_2() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms";
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
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40055-1-1
     */
    private List<SqlTask> xxIndex40055_1_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx NOT LIKE \"4*\" AND djzclx IS NOT NULL AND djzclx!=\"\" AND djzclx!=0 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx NOT LIKE \"4*\" AND djzclx IS NOT NULL AND djzclx!=\"\" AND djzclx!=0 {time_condition} GROUP BY  xnzz_id,swjg_dms";
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
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1-2
     */
    private List<SqlTask> xxIndex40054_1_2() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx LIKE \"4*\"  {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms";
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
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1-1
     */
    private List<SqlTask> xxIndex40054_1_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx NOT LIKE \"4*\" AND djzclx IS NOT NULL AND djzclx!=\"\" AND djzclx!=0 GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) AND djzclx NOT LIKE \"4*\" AND djzclx IS NOT NULL AND djzclx!=\"\" AND djzclx!=0 GROUP BY  xnzz_id,swjg_dms";
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
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40066-1
     */
    private List<SqlTask> xxIndex40066_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40062")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40062-1
     */
    private List<SqlTask> xxIndex40062_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40062")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40055-1
     */
    private List<SqlTask> xxIndex40055_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE xxzt = 1 AND tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40055")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40054-1
     */
    private List<SqlTask> xxIndex40054_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(nsr_id) FROM bigdata_xxzx_xxmx WHERE tsdx=3 and fszt=1 AND xxywlx = 3 AND and czzt = 6 and xxch = 0  AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40054")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 消息—40053-1
     */
    private List<SqlTask> xxIndex40053_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND xxly = 9 AND and czzt = 6  and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND xxly = 9 AND and czzt = 6  and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40053")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        List<SqlTask> cjsj = enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        //组织
        return cjsj;
    }

    /**
     * 消息—40052-1
     */
    private List<SqlTask> xxIndex40052_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND and czzt = 6 and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(xxzb_id) FROM bigdata_xxzx_xxzb WHERE xxywlx = 3 AND and czzt = 6 and xxch = 0 and parent_id != 0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40052")
                .setDim1("1001")
                .setDim2("1005")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10030-5
     */
    private List<SqlTask> nsrIndex10030_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=2 GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=2 GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10030")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10030-1
     */
    private List<SqlTask> nsrIndex10030_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=2 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=2 {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10030")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10010-5
     */
    private List<SqlTask> nsrIndex10010_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10010-1
     */
    private List<SqlTask> nsrIndex10010_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(DISTINCT yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(DISTINCT yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2)  {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10020-1
     */
    private List<SqlTask> nsrIndex10020_1() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND jhbj=1  swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—10020-5
     */
    private List<SqlTask> nsrIndex10020_5() throws Exception {
        String selfSql = "SELECT xnzz_id,swjg_dm,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dm";
        String superiorSql = "SELECT xnzz_id,swjg_dms,COUNT(yh_id) FROM bigdata_yhzx_xnzz_yh  WHERE is_delete=0 AND swjgbz IN (1,2) AND cysx=1 {time_condition} GROUP BY  xnzz_id,swjg_dms";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("10020")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20040-1
     */
    private List<SqlTask> nsrIndex20040_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20040")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20040-5
     */
    private List<SqlTask> nsrIndex20040_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20040")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20070-1
     */
    private List<SqlTask> nsrIndex20070_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20070")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20070-5
     */
    private List<SqlTask> nsrIndex20070_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20070")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20010-1
     */
    private List<SqlTask> nsrIndex20010_1() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("1");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 纳税人—20010-5
     */
    private List<SqlTask> nsrIndex20010_5() throws Exception {
        //组织
        String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("20010")
                .setDim1("1001")
                .setDim2("1004")
                .setDimData2("5");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
    }

    /**
     * 群组—40040
     */
    private List<SqlTask> qzIndex40040() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select xnzz_id,swjg_dms,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40040")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }

    /**
     * 群组—40042
     */
    private List<SqlTask> qzIndex40042() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select xnzz_id,swjg_dms,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40042")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }

    /**
     * 群组—40043
     */
    private List<SqlTask> qzIndex40043() throws Exception {
        //组织
        String selfSql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
        String superiorSql = "select xnzz_id,swjg_dms,count(nsr_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
        //通用字段提取
        CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                .builder()
                .setIndex("40043")
                .setDim1("1001");
        /*----------------------------------------------action-----------------------------------------------*/
        //组织
        return enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
    }
    /********************************************************************************************************************************************************************************************************************
     ******************************************************************************************method to get sqlTask**************************************************************************************************
     **********************************************************************************************************************************************************************************************************************/
    /**
     * 入口
     *
     * @param selfSql                     本级sql
     * @param superiorSql                 上级sql
     * @param commonFiledExtractorBuilder 公共字段提取
     * @param isQz                        是否为群组指标
     * @throws Exception 异常
     */
    private List<SqlTask> enter(String selfSql, String superiorSql, CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder, Boolean isQz, String timeFiled) throws Exception {
        List<AggregationSpecialFiledExtractor> specialSelfAgg = getSpecialSelfAgg(isQz);
        List<AggregationSpecialFiledExtractor> specialSuperiorAgg = getSpecialSuperiorAgg(isQz);
        List<CommonFiledExtractor> commonAgg = getCommonAgg(commonFiledExtractorBuilder, isQz);
        List<SqlTask> selfSqlTasks =
                SqlTransformDslUtil.doPreTransform(selfSql, true, !isQz, true, specialSelfAgg, commonAgg, timeFiled);
        List<SqlTask> superiorSqlTasks =
                SqlTransformDslUtil.doPreTransform(superiorSql, true, !isQz, true, specialSuperiorAgg, commonAgg, timeFiled);
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
    private List<AggregationSpecialFiledExtractor> getSpecialSelfAgg(Boolean isQz) throws Exception {
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithTotalAndSelf());
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractorInc = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithIncAndSelf());
        List<AggregationSpecialFiledExtractor> aggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractor);
        if (!isQz)
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        return aggregationSpecialFiledExtractorArrayList;
    }

    /**
     * 获得特殊的上级字段提取器
     *
     * @param isQz 是否为群组
     * @return 特殊的上级字段提取器集合（包含，当天，30天，当月）
     */
    private List<AggregationSpecialFiledExtractor> getSpecialSuperiorAgg(Boolean isQz) throws Exception {
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithTotalAndSuperior());
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractorInc = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithIncAndSuperior());
        List<AggregationSpecialFiledExtractor> aggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractor);
        if (!isQz)
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        return aggregationSpecialFiledExtractorArrayList;
    }
}
