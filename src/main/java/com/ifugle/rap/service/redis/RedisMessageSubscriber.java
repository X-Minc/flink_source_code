/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.redis;

import java.util.concurrent.BlockingQueue;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ifugle.rap.cache.AppPubSubService;
import com.ifugle.rap.cache.AppSubscriber;

/**
 * 接收税小蜜消息订阅处理
 *
 * @author HuangLei(wenyuan)
 * @version $Id RedisMessageSubscriber.java v 0.1 2018/11/9 HuangLei(wenyuan) Exp $
 */
@Component
public class RedisMessageSubscriber  implements AppSubscriber {

    private final static Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);


    @Autowired
    private AppPubSubService appPubSubService;

    /**
     * 元数据仓库
     */
    private BlockingQueue<String> dataContainer;


    /***
     * 执行消息订阅
     */
    public void initiateSubscribe() {
        this.appPubSubService.subscribe(ParseConstant.REDIS_CHANNEL_NAME, ParseConstant.SUBSCRIBER_NAME, this);
    }

    /***
     * 接收消息处理业务逻辑
     * @param message
     * @return
     */
    public boolean onMessage(String message) {
        logger.info("[RedisMessageSubscriber] recieve message = "+message);
        dataContainer.add(message);
        return true;
    }

    /***
     * 单个发送
     * @param topic
     * @param message
     */
    public void sendMessage(String topic,String message){
        if(logger.isInfoEnabled()){
            logger.info("[RedisMessageSubscriber] send message = "+message+",topic="+topic);
        }
        appPubSubService.publish(topic,message);
    }

    /***
     * 批量发送
     * @param topic
     * @param messages
     */
    public void sendMessageBatch(String topic,String... messages){
        if(logger.isInfoEnabled()){
            logger.info("[RedisMessageSubscriber] send message = "+ JSON.toJSONString(messages)+",topic="+topic);
        }
        if(messages!=null&& messages.length != 0) {
            for (String mes : messages) {
                if(StringUtils.isNotBlank(mes)) {
                    appPubSubService.publish(topic, mes);
                }
            }
        }

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
