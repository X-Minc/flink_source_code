package com.ifugle.rap.service.rocketmq;

import com.aliyun.openservices.ons.api.*;
import com.google.gson.Gson;
import com.ifugle.rap.model.shuixiaomi.EsDocumentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Properties;


@Component
public class RocketMqProducter {

    @Value("${mq.rocket.groupId}")
    private String groupId;
    @Value("${mq.rocket.username}")
    private String accessKey;
    @Value("${mq.rocket.password}")
    private String secretKey;
    @Value("${mq.rocket.server}")
    private String nameServer;
    @Value("${mq.rocket.topic}")
    private String mqTopic;

    @Value("${mq.rocket.groupId.Two}")
    private String groupId2;
    @Value("${mq.rocket.server.Two}")
    private String nameServer2;
    @Value("${mq.rocket.topic.Two}")
    private String mqTopic2;

    @Value("${mq.rocket.groupId.Three}")
    private String groupId3;
    @Value("${mq.rocket.server.Three}")
    private String nameServer3;
    @Value("${mq.rocket.topic.Three}")
    private String mqTopic3;

    private final static Logger logger = LoggerFactory.getLogger(RocketMqProducter.class);

    /***
     * 发送消息
     * @param messageIds
     */
    public void sendMessage(String messageIds) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,nameServer);

        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();

        EsDocumentData esDocumentData = new Gson().fromJson(messageIds, EsDocumentData.class);
        StringBuffer stringBuffer = new StringBuffer();
        if (esDocumentData != null) {
            List<Long> ids = esDocumentData.getIds();
            for (int i = 0; i < ids.size(); i++) {
                if (i == ids.size() - 1) {
                    stringBuffer.append(ids.get(i));
                } else {
                    stringBuffer.append(ids.get(i) + ",");
                }
            }
        }
        //循环发送消息
        Message msg = new Message( //
                // Message 所属的 Topic
                mqTopic,
                // Message Tag 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在 MQ 服务器过滤
                esDocumentData.getIndexName(),
                // Message Body 可以是任何二进制形式的数据， MQ 不做任何干预，
                // 需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
                messageIds.getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("ORDERID_" + stringBuffer.toString());
        logger.info(String.format("rocketMq parameter groupID=%s,AccessKey=%s,SecretKey=%s,NameServer=%s,topic=%s,message=%s", groupId, accessKey, secretKey, nameServer, mqTopic, messageIds));
        try {
            SendResult sendResult = producer.send(msg);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                logger.info(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
            }
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            logger.info(new Date() + " Send mq message failed. Topic is:" + msg.getTopic(), e);
        }

        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
    }

    /***
     * 发送消息
     * @param messageIds
     */
    public void sendKbsQuestionMessage(String messageIds) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId2);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,nameServer2);

        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();

        EsDocumentData esDocumentData = new Gson().fromJson(messageIds, EsDocumentData.class);
        StringBuffer stringBuffer = new StringBuffer();
        if (esDocumentData != null) {
            List<Long> ids = esDocumentData.getIds();
            for (int i = 0; i < ids.size(); i++) {
                if (i == ids.size() - 1) {
                    stringBuffer.append(ids.get(i));
                } else {
                    stringBuffer.append(ids.get(i) + ",");
                }
            }
        }
        //循环发送消息
        Message msg = new Message( //
                // Message 所属的 Topic
                mqTopic2,
                // Message Tag 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在 MQ 服务器过滤
                esDocumentData.getIndexName(),
                // Message Body 可以是任何二进制形式的数据， MQ 不做任何干预，
                // 需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
                messageIds.getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("ORDERID_" + stringBuffer.toString());
        logger.info(String.format("rocketMq kbs_question_msg parameter groupID=%s,AccessKey=%s,SecretKey=%s,NameServer=%s,topic=%s,message=%s", groupId2, accessKey, secretKey, nameServer2, mqTopic2, messageIds));
        try {
            SendResult sendResult = producer.send(msg);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                logger.info(new Date() + " Send kbs_question_msg mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
            }
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            logger.info(new Date() + " Send kbs_question_msg mq message failed. Topic is:" + msg.getTopic(), e);
        }

        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
    }


    /***
     * 发送消息
     * @param messageIds
     */
    public void sendBotScaTaskResultMessage(String messageIds) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId3);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, RocketMqConstants.AccessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, RocketMqConstants.SecretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,nameServer3);

        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();

        EsDocumentData esDocumentData = new Gson().fromJson(messageIds, EsDocumentData.class);
        StringBuffer stringBuffer = new StringBuffer();
        if (esDocumentData != null) {
            List<Long> ids = esDocumentData.getIds();
            for (int i = 0; i < ids.size(); i++) {
                if (i == ids.size() - 1) {
                    stringBuffer.append(ids.get(i));
                } else {
                    stringBuffer.append(ids.get(i) + ",");
                }
            }
        }
        //循环发送消息
        Message msg = new Message( //
                // Message 所属的 Topic
                mqTopic3,
                // Message Tag 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在 MQ 服务器过滤
                esDocumentData.getIndexName(),
                // Message Body 可以是任何二进制形式的数据， MQ 不做任何干预，
                // 需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
                messageIds.getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("ORDERID_" + stringBuffer.toString());
        logger.info(String.format("rocketMq bot_sca_task_result_msg parameter groupID=%s,AccessKey=%s,SecretKey=%s,NameServer=%s,topic=%s,message=%s", groupId3, RocketMqConstants.AccessKey, RocketMqConstants.SecretKey, nameServer3, mqTopic3, messageIds));
        try {
            SendResult sendResult = producer.send(msg);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                logger.info(new Date() + " Send bot_sca_task_result_msg mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
            }
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            logger.info(new Date() + " Send bot_sca_task_result_msg mq message failed. Topic is:" + msg.getTopic(), e);
        }

        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
    }
}
