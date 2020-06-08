/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.elasticsearch.service;

import com.ifugle.rap.elasticsearch.model.DataRequest;

/**
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: ElasticSearchBusinessApi.java, v 0.1 2018年5月15日 上午11:28:41 HuangLei(wenyuan) Exp $
 */
public interface ElasticSearchBusinessApi {

    /***
     * 数据导入请求api
     * 
     * @param request
     * @return
     */
    public boolean exportDataMysqlToEs(String channelType, DataRequest request);

    /**
     * @auther: Liuzhengyang
     * 判断数据是否存在在ES中
     */
    public boolean checkDataExistsInEs(String channelType,DataRequest request);
}
