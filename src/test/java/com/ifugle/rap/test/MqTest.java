package com.ifugle.rap.test;

import com.ifugle.rap.service.rocketmq.RocketMqProducter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MqTest extends BaseTest {

    @Autowired
    RocketMqProducter rocketMqProducter;

    @Test
    public void testSend(){
        rocketMqProducter.sendMessage("huanglei");
    }
}
