/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.dsb.message;

import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id Subscriber.java v 0.1 2019/7/29 HuangLei(wenyuan) Exp $
 */
@Service
public class DsbSubscriber extends JedisPubSub {
    public DsbSubscriber(){

    }
    @Override
    public void onMessage(String channel, String message) {    //收到消息会调用
        System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {  //订阅了频道会调用
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {  //取消订阅 会调用
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
}

