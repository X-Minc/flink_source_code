package com.ifugle.rap.data.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.ifugle.rap.bigdata.task.service.RealTimeUpdateTaskService;

/**
 * @author WenYuan
 * @version $
 * @since 4月 25, 2021 10:11
 */
@RestController
@RequestMapping(value = "/")
public class ApplicationController {

    @Autowired
    private RealTimeUpdateTaskService realTimeUpdateTaskService;


    @GetMapping("/hc")
    public String healthy(){
        return "OK";
    }

    /**
     * 抽取增量数据到ES并做增量量标签更新和数据汇总
     * @param startTime
     * @return
     */
    @RequestMapping(value = "/getUpdateDataToEs", method = RequestMethod.GET)
    public Map<String, Object> getUpdateDataToEs(String startTime) {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        realTimeUpdateTaskService.getUpdateDataToEs(startTime);
        result.put("success", true);
        return result;
    }
}
