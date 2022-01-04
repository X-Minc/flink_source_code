package com.ifugle.rap.data.task;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Minc
 * @date 2021/12/30 14:20
 */
@Component
public class TestTask {
    @Autowired
    BulkTemplateRepository<Map<String, Object>> esTemplateRepository;


    @Scheduled(fixedDelay = 1000L * 60 * 30)
    public void getQuery() {
        try {
            String sql = "select 0 as xnzz_id,swjg_dms,count(*) from user_all_tag  group by xnzz_id,bm_id limit 0 glimit 100000";
            SqlEntry sqlEntry = SqlTransformDslUtil.getTransformedSqlEntry(sql);
            String dsl = SqlTransformDslUtil.getTransformedDsl(sqlEntry, new GroupBySqlTransformRule(), new WhereSqlTransformRule());
            JSONObject resultJsonObj = esTemplateRepository.queryListByDSL(sqlEntry.getFrom().getValue(), dsl, JSONObject::parseObject);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
