package com.ifugle.rap.task;


import com.ifugle.rap.common.lang.util.DateUtils;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CompanyActiveTask {

    private final static Logger logger = LoggerFactory.getLogger(CompanyActiveTask.class);

    @Autowired
    YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    @Autowired
    ElasticSearchBusinessService elasticSearchBusinessService;

    /**
     * 每天凌晨执行一次
     */
    @Scheduled(cron = "0 15 0 * * ?")
    private void process() {
        try {
            logger.info("###### this is scheduler task running repair active data ");
            Date date = DateUtil.dateAdd(new Date(), "dd", -2);
            String startTime = DateUtil.format(date, DateUtil.ISO8601_DATE) + " 00:00:00";
            int pageIndex = 1;
            int pageSize = 10000;
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<YhzxXnzzNsr> yhzxXnzzNsrs = yhzxXnzzNsrMapper.selectYhzxXnzzNsrForSync(startTime, first, pageSize);
                if (!CollectionUtils.isEmpty(yhzxXnzzNsrs)) {
                    StringBuffer DSL = new StringBuffer(32);
                    for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
                        DataRequest request = new DataRequest();
                        request.setCatalogType("YHZX_XNZZ_NSR");
                        Map map = new HashMap();
                        map.put("ID", yhzxXnzzNsr.getId());
                        map.put("JHBJ", yhzxXnzzNsr.getJhbj());
                        request.setMap(map);
                        DSL.append(elasticSearchBusinessService.formatUpdateDSL("dingtax", request));
                    }
                    logger.info("###### this is scheduler task dsl " + DSL.toString() + ",第" + pageIndex + "次执行");
                    elasticSearchBusinessService.bulkOperation(DSL.toString());
                } else {
                    logger.info("###### this is scheduler task complete .....");
                    break;
                }
                pageIndex++;
            }
        }catch (Exception e){
            logger.error("执行定时任务异常",e);
        }
    }

}
