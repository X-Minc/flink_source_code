package com.ifugle.rap.data.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

    @Autowired
    DataSyncClient dataSyncClient;

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
        initPrpperties();
        logger.info("任务{}未设置自动启动。", "dsb");
        logger.info("dataInit start");
        dataInitClient.init();

        logger.info("dataSync start");
        dataSyncClient.sync();
    }



    public void initPrpperties(){
        System.setProperty(ConfigParamConstant.RAP_ES_HOST,hostUrl);
        System.setProperty(ConfigParamConstant.RAP_ES_USERNAME,username);
        System.setProperty(ConfigParamConstant.RAP_ES_PASSWORD,password);
        System.setProperty(ConfigParamConstant.RAP_ES_CONNECTION_TIMEOUT,String.valueOf(connectTimeout));
        System.setProperty(ConfigParamConstant.RAP_ES_SOCKET_TIMEOUT,String.valueOf(socketTimeout));
        System.setProperty(ConfigParamConstant.RAP_ES_MAX_RETRY_TIMEOUT,String.valueOf(maxRetryTimeoutMillis));
        System.setProperty(ConfigParamConstant.RAP_ES_MAX_CONN_TOTAL,String.valueOf(maxConnTotal));
        System.setProperty(ConfigParamConstant.RAP_ES_MAX_CONN_PER_ROUTE,String.valueOf(maxConnPerRout));
    }

}
