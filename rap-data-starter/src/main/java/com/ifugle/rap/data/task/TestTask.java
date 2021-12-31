//package com.ifugle.rap.data.task;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
//import com.ifugle.rap.elasticsearch.entity.QueryEntity;
////import com.ifugle.rap.utils.sqltransformutil.RuleBeanUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * @author Minc
// * @date 2021/12/30 14:20
// */
//@Component
//public class TestTask {
//    @Autowired
//    BulkTemplateRepository<Map<String, Object>> esTemplateRepository;
//
//
//    @Scheduled(fixedDelay = 1000L * 60 * 30)
//    public void getQuery() {
//        try {
//            String sql = "select xnzz_id,bm_id,count(*) from user_all_tag group by xnzz_id,bm_id";
////            String dsl = ruleBeanUtil.sqlTransformDslUtil().sqlTransformDsl(sql, 0, 0);
////            String indexName = ruleBeanUtil.sqlTransformDslUtil().getIndexName(sql);
////            JSONObject jsonObject = esTemplateRepository.queryListByDSL(
////                    indexName,
////                    dsl,
////                    JSONObject::parseObject);
////            System.out.println(jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
