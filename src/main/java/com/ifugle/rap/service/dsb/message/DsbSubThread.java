/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.dsb.message;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id DsbSubThread.java v 0.1 2019/7/29 HuangLei(wenyuan) Exp $
 */
@Service
public class DsbSubThread extends Thread {

    @Autowired
    DsbSubscriber dsbSubscriber;

    @Value("${dsb.redis.host}")
    String redisHost;

    @Value("${dsb.redis.password}")
    String redisPassword;

    @Value("${dsb.redis.port}")
    Integer redisPort;

    private final static Logger logger  = LoggerFactory.getLogger(DsbSubThread.class);

    @Override
    public void run() {
        logger.info(String.format("dsb-subscribe-redis, channel %s, thread will be blocked", DsbConstant.channel));
        if(StringUtils.isBlank(redisPassword)){
            redisPassword=null;
        }
        Jedis jedis = null;
        try {
            JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort,DsbConstant.timeout,redisPassword);
            jedis = jedisPool.getResource();  //取出一个连接
            jedis.subscribe(dsbSubscriber, DsbConstant.channel);  //通过subscribe 的api去订阅，入参是订阅者和频道名
        } catch (Exception e) {
            logger.info(String.format("subsrcibe channel error, %s", e));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        logger.info("dsb redis pub-sub model start successful ... ");
    }

}
