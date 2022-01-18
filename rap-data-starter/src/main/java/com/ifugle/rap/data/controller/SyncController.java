package com.ifugle.rap.data.controller;

import com.google.common.collect.Maps;
import com.ifugle.rap.data.task.sync.SyncTask;
import com.ifugle.rap.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Minc
 * @date 2022/1/12 17:40
 */
@RestController
@RequestMapping(value = "/sync")
public class SyncController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    SyncTask syncTask;

    @RequestMapping(value = "/manual", method = RequestMethod.GET)
    public Map<String, Object> manualSync() {
        LOGGER.info("时间={},调用同步接口", TimeUtil.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> result = new HashMap<>();
        try {
            syncTask.setTaskAndRun();
            result.put("200", "调用成功");
        } catch (Exception e) {
            result.put("500", "调用失败！请查看日志");
            LOGGER.error("收到调用接口产生错误！", e);
        }
        return result;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Map<String, Object> initSync() {
        LOGGER.info("时间={},调用初始化接口", TimeUtil.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> result = new HashMap<>();
        try {
            syncTask.init();
            result.put("200", "调用成功");
        } catch (Exception e) {
            result.put("500", "调用失败！请查看日志");
            LOGGER.error("收到调用接口产生错误！", e);
        }
        return result;
    }
}
