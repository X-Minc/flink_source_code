package com.ifugle.rap.data.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.ifugle.rap.elasticsearch.core.ConfigParamConstant;

/**
 * @author ifugle
 * @version $
 * @since 4月 08, 2021 21:33
 */
@Component
public class StartAllJobInit  implements CommandLineRunner {

    protected Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    DataInitClient dataInitClient;

    @Autowired(required = false)
    DataSyncDsbClient dataSyncDsbClient;

    @Autowired(required = false)
    DataSyncBotClient dataSyncBotClient;

    /** 连接超时，默认：5000ms */
    @Value("${es.connect.timeout}")
    private int connectTimeout ;

    /** 接口超时，默认：6000ms */
    @Value("${es.socket.timeout}")
    private int socketTimeout ;

    /** 连接最大重试时间，默认：60000ms */
    @Value("${es.max.retry.timeout}")
    private int maxRetryTimeoutMillis ;

    @Value("${es.url}")
    private String hostUrl;

    /** 登录用户名 */
    @Value("${es.username}")
    private String username;

    /** 登录用户密码 */
    @Value("${es.password}")
    private String password;

    @Value("${es.max.conn.total}")
    private int maxConnTotal ;

    @Value("${es.max.conn.per.route}")
    private int maxConnPerRout ;

    @Override
    public void run(String... args) throws Exception {
        //初始化连接
        logger.info("任务{}未设置自动启动。", "dsb");
        logger.info("dataInit start");
        dataInitClient.init();

        logger.info("dataSync start");
        if(dataSyncDsbClient!=null) {
            // dataSyncDsbClient.sync(); //同步丁税宝
        }
        if(dataSyncBotClient!=null) {
            // dataSyncBotClient.syncBot(); //同步税小蜜
        }
    }

}
