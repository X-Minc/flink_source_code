package com.ifugle.rap.service.rocketmq;

public class RocketMqConstants {

    public final static String GROUP_ID = System.getProperty("rap.mq.rocket.groupId","CID_rap_bot_es_dev");

    public final static String AccessKey = System.getProperty("rap.mq.rocket.username","duLCaybnI9oOyOdZ");

    public final static String SecretKey  =  System.getProperty("rap.mq.rocket.password","RCHE8NY4DkIldIei3O4ub61FwxEaGa");

    public final static String NameServer = System.getProperty("rap.mq.rocket.server","http://onsaddr.mq-internet-access.mq-internet.aliyuncs.com:80");

    public final static String MQ_TOPIC = System.getProperty("rap.mq.rocket.topic","topic_bot_data_sync");

//    public final static String GROUP_ID = System.getProperty("rap.mq.rocket.groupId","CID_rap_bot_es");
//
//    public final static String AccessKey = System.getProperty("rap.mq.rocket.username","duLCaybnI9oOyOdZ");
//
//    public final static String SecretKey  =  System.getProperty("rap.mq.rocket.password","RCHE8NY4DkIldIei3O4ub61FwxEaGa");
//
//    public final static String NameServer = System.getProperty("rap.mq.rocket.server","http://onsaddr.cn-hangzhou.mq-internal.aliyuncs.com:8080");
//
//    public final static String MQ_TOPIC = System.getProperty("rap.mq.rocket.topic","topic_bot_data_sync_test");

}
