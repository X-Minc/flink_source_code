/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.redis;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.model.enums.TablesEnum;
import com.ifugle.rap.service.redis.ChangedPropertyData;
import com.ifugle.util.JSONUtil;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id JsonConvertTest.java v 0.1 2018/11/12 HuangLei(wenyuan) Exp $
 */
public class JsonConvertTest {

   /* public static void main(String[] args) {
        String json = "{\"ids\":[396246,12],\"properties\":{\"TAGS\":[11114,11114,11086,11086,10155,10155,10135,11090,11219,11010,11093,11168]}}";
        ChangedPropertyData changedPropertyData = new Gson().fromJson(json,ChangedPropertyData.class);
        List<Double> list =  (List<Double>)changedPropertyData.getProperties().get("TAGS");
        for (Double id: list ) {
            System.out.println(id.longValue());
        }
    }
*/

    public static void main(String[] args) {

        String message =  "{\"ids\":[379377],\"docName\":\"KBS_QUESTION\",\"properties\":{\"TAGS\":[11086,11086,10135,11090,11219,11010,11093,11168,11218,11119,11058],\"QUOTED_ARTICLES\":[\"12\",\"456\"]}}";
        ChangedPropertyData changedPropertyData = new Gson().fromJson(message,ChangedPropertyData.class);
        //获取要更新的id的list
        List<Long> list = changedPropertyData.getIds();
        StringBuffer DSL = new StringBuffer(256);
        String docName =changedPropertyData.getDocName();
        for (Long id: list ) {
            DataRequest request = compriseDataRequest(id, changedPropertyData.getProperties(), TablesEnum.TABLES.get(docName.toLowerCase()));
            ChannelType channelType = TablesEnum.TABLE_CHANNEL.get(docName.toLowerCase());
            DSL.append(formatUpdateDSL(channelType, request));
        }
        if (DSL.length() > 0) {
            System.out.println(DSL.toString());
        }
    }


    /***
     * 构建请求request
     * @param id
     * @param attrs
     * @param tables
     * @return
     */
    private static DataRequest compriseDataRequest(Long id, Map<String, Object> attrs, TablesEnum tables) {
        DataRequest request = new DataRequest();
        request.setCatalogType(tables.getTableName());
        Map<String, Object> hashMap = new HashMap<String, Object>(16);
        hashMap.put("ID", id);
        for (String key : attrs.keySet()) {
            Object object = attrs.get(key);
            hashMap.put(key, object);
        }
        request.setMap(hashMap);
        return request;
    }


    /**
     * 得到更新的DSL
     *
     * @param channelType
     * @param request
     *
     * @return
     */
    public static String formatUpdateDSL(ChannelType channelType, DataRequest request) {
        String SAMPLE_UPDATE_DSL = "{ \"update\": { \"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\"} } \n\r" + "{ \"doc\" : %s }\n\r";
        Map<String, Object> map = request.getMap();
        String ids = request.getMap().containsKey("ID") ? request.getMap().get("ID").toString() : request.getMap().get("id").toString();
        String data = JSONUtil.toJSON(map);
        StringBuffer DSL = new StringBuffer(String.format(SAMPLE_UPDATE_DSL, channelType.getCode(), request.getCatalogType(), ids, data));
        return DSL.toString();
    }

    @Test
    public void initTests(){

    }
}
