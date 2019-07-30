/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.dsb.message;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.model.enums.TablesEnum;
import com.ifugle.rap.service.redis.MessageConsumer;

import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id Subscriber.java v 0.1 2019/7/29 HuangLei(wenyuan) Exp $
 */
@Service
public class DsbSubscriber extends JedisPubSub {

    private final static Logger logger  = LoggerFactory.getLogger(DsbSubscriber.class);

    @Autowired
    DsbDelOrUpdateService dsbDelOrUpdateService;

    @Autowired
    ElasticSearchBusinessService elasticSearchBusinessService;

    /***
     * message的格式需定义成：删除行为: delete:id  update行为：update:id:json字符串
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {    //收到消息会调用
        logger.info(String.format("receive dsb-onMessage redis published message, channel %s, message %s", channel, message));
        //处理定义的格式
        if(StringUtils.isNotBlank(message)){
            String[] messageArr = message.split("\\|");
            if(messageArr.length==3&&StringUtils.equalsIgnoreCase(messageArr[0],"delete")){
                String id = messageArr[2];
                if(NumberUtils.isDigits(id)&&StringUtils.isNotBlank(id)){
                    dsbDelOrUpdateService.deleteYhzxXnzzNsr(id,messageArr[1]);
                }else{
                    logger.error("dsb sync data error data type: id");
                }

            }else if(messageArr.length==4&&StringUtils.equalsIgnoreCase(messageArr[0],"update")){
                String id = messageArr[2];
                String tableName=  messageArr[1];
                String json = messageArr[3];
                if(NumberUtils.isDigits(id)&&StringUtils.isNotBlank(id)){
                    Map map =null;
                    try {
                        Type mapType = new TypeToken<Map<String, String>>() {
                        }.getType();
                        map = new Gson().fromJson(json, mapType);
                    }catch (Exception e){
                        logger.error("error data json2map,e=",e);
                    }
                    requestData(id,tableName,map);
                }else{
                    logger.error("dsb sync data error data type: id");
                }
            }else{
                logger.error("dsb sync data error action type,please check is having delete or update");
            }
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {  //订阅了频道会调用
        logger.info(String.format("subscribe dsb-onSubscribe redis channel success, channel %s, subscribedChannels %d", channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {  //取消订阅 会调用
        logger.info(String.format("unsubscribe dsb-onUnsubscribe redis channel, channel %s, subscribedChannels %d", channel, subscribedChannels));
    }

    private void requestData(String keyid,String docName,Map map) {
        //获取要更新的id的list
        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(keyid));
        StringBuilder dsl = new StringBuilder(256);
        for (Long id: list ) {
            DataRequest request = MessageConsumer.compriseDataRequest(id, map, TablesEnum.TABLES.get(docName.toLowerCase()));
            logger.debug(MessageFormat.format("转化DataRequest成功,{0}", request));
            ChannelType channelType = TablesEnum.TABLE_CHANNEL.get(docName.toLowerCase());
            dsl.append(MessageConsumer.formatUpdateDSL(channelType, request));
        }
        if (dsl.length() > 0) {
            logger.debug(String.format("订阅服务发送ES信息，%s", dsl.toString()));
            elasticSearchBusinessService.bulkOperation(dsl.toString());
        }
    }

}

