package com.ifugle.rap.test;

import com.alibaba.fastjson.JSON;
import com.ifugle.rap.model.shuixiaomi.EsDocumentData;
import com.ifugle.rap.service.rocketmq.RocketMqProducter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MqTest extends BaseTest {

    @Autowired
    RocketMqProducter rocketMqProducter;

    @Test
    public void testSend(){
        List<Long> ids = new ArrayList<>();
        ids.add(294901L);
        EsDocumentData esDocumentData = new EsDocumentData(ids,"doc","bot_chat_request");
        rocketMqProducter.sendMessage(JSON.toJSONString(esDocumentData));
    }
}
