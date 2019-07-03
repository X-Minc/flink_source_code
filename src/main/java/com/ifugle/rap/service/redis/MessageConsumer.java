/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.redis;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.model.enums.TablesEnum;
import com.ifugle.util.JSONUtil;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id MessageConsumer.java v 0.1 2018/11/21 HuangLei(wenyuan) Exp $
 */
@Component
public class MessageConsumer extends Thread {

    /**
     * 数据仓库
     */
    private BlockingQueue<String> dataContainer;

    private Integer               sleepTime = 10;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    private final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    /***
     * 执行消费线程
     */
    @Override
    public void run(){
        while (true) {
            try {
                logger.info("[PackageConsumer start running ........]");
                //从仓库获取待消费的数据
                String curData = dataContainer.poll(2, TimeUnit.SECONDS);
                try {
                    if(StringUtils.isBlank(curData)){
                        continue;
                    }
                    requestData(curData);
                    //拉包休息1s
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    logger.error("MessageConsumer run error", e);
                }
            } catch (InterruptedException e) {
                logger.error("MessageConsumer run error", e);
            }
        }
    }

    private void requestData(String message) {
        ChangedPropertyData changedPropertyData = new Gson().fromJson(message,ChangedPropertyData.class);
        //获取要更新的id的list
        List<Long> list = changedPropertyData.getIds();
        StringBuilder dsl = new StringBuilder(256);
        String docName =changedPropertyData.getDocName();
        for (Long id: list ) {
            DataRequest request = compriseDataRequest(id, changedPropertyData.getProperties(), TablesEnum.TABLES.get(docName.toLowerCase()));
            logger.debug(MessageFormat.format("转化DataRequest成功,{0}", request));
            ChannelType channelType = TablesEnum.TABLE_CHANNEL.get(docName.toLowerCase());
            dsl.append(formatUpdateDSL(channelType, request));
        }
        if (dsl.length() > 0) {
            logger.debug(String.format("订阅服务发送ES信息，%s", dsl.toString()));
            elasticSearchBusinessService.bulkOperation(dsl.toString());
        }
    }

    /***
     * 构建请求request
     * @param id
     * @param attrs
     * @param tables
     * @return
     */
    private DataRequest compriseDataRequest(Long id, Map<String, Object> attrs, TablesEnum tables) {
        DataRequest request = new DataRequest();
        request.setCatalogType(tables.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", id);
        for (String key : attrs.keySet()) {
            Object object = attrs.get(key);
            hashMap.put(key, object);
        }
        request.setMap(hashMap);
        return request;
    }


    /**
     * 得到更新的DSL
     *
     * @param channelType
     * @param request
     *
     * @return
     */
    private String formatUpdateDSL(ChannelType channelType, DataRequest request) {
        String sampleUpdateDsl = "{ \"update\": { \"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\"} } \n\r" + "{ \"doc\" : %s }\n\r";
        Map<String, Object> map = request.getMap();
        String ids = request.getMap().containsKey("ID") ? request.getMap().get("ID").toString() : request.getMap().get("id").toString();
        String data = JSONUtil.toJSON(map);
        return String.format(sampleUpdateDsl, channelType.getCode(), request.getCatalogType(), ids, data);
    }

    /**
     * Getter method for property <tt>dataContainer</tt>.
     *
     * @return property value of dataContainer
     */
    public BlockingQueue<String> getDataContainer() {
        return dataContainer;
    }

    /**
     * Setter method for property <tt>dataContainer</tt>.
     *
     * @param dataContainer value to be assigned to property dataContainer
     */
    public void setDataContainer(BlockingQueue<String> dataContainer) {
        this.dataContainer = dataContainer;
    }
}
