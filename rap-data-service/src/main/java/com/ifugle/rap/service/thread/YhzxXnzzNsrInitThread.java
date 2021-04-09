/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
public class YhzxXnzzNsrInitThread extends BaseInitThread implements Runnable {

    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    private static final Logger logger = LoggerFactory.getLogger(YhzxXnzzNsrInitThread.class);

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        List<YhzxXnzzNsr> yhzxXnzzNsrs = new ArrayList<YhzxXnzzNsr>();
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            try {
                yhzxXnzzNsrs = yhzxXnzzNsrMapper.selectYhzxXnzzNsrForInit(first, pageSize);
                logger.info("[KbsArticleInitThread] thread operate database size = " + yhzxXnzzNsrs.size());
                if (checkExit(yhzxXnzzNsrs)) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error("KbsArticleInitThread sleep error", ex);
            }
            pageIndex++;
        }
        writeLocalTime(yhzxXnzzNsrs);
    }

    public YhzxXnzzNsrInitThread(Integer pageSize, SyncService syncService, YhzxXnzzNsrMapper yhzxXnzzNsrMapper) {
        super(pageSize, syncService);
        this.yhzxXnzzNsrMapper = yhzxXnzzNsrMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("KBS_ARTICLE already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<YhzxXnzzNsr> yhzxXnzzNsrs) {
        if (CollectionUtils.isEmpty(yhzxXnzzNsrs)) {
            return true;
        }
        if (syncService.insertYhzxXnzzNsrAndCheckListSize(yhzxXnzzNsrs, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<YhzxXnzzNsr> yhzxXnzzNsrs) {
        if (yhzxXnzzNsrs.size() > 0) {
            Date createDate = yhzxXnzzNsrs.get(yhzxXnzzNsrs.size() - 1).getCjsj();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkYhzxXnzzNsrTimeEquals(yhzxXnzzNsrs)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "YHZX_XNZZ_NSR");
        }
    }

    public static void main(String[] args) {
        System.out.println(new CompriseUtils().transportData(new Date().toString()));
    }
}
