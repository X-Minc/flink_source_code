/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BizDataMapper;
import com.ifugle.rap.model.shuixiaomi.BizData;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
public class BotBizDataInitThread extends BaseInitThread implements Runnable {

    private BizDataMapper bizDataMapper;

    private static final Logger logger = LoggerFactory.getLogger(BotBizDataInitThread.class);

    @Override
    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        List<BizData> bizDataList;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            try {
                bizDataList = bizDataMapper.selectBotBizDataForInit(first, pageSize);
                logger.info("[BotBizDataInitThread] thread operate database size = " + bizDataList.size());
                if (checkExit(bizDataList)) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error("BotBizDataInitThread sleep error", ex);
            }
            pageIndex++;
        }
        writeLocalTime(bizDataList);
    }

    public BotBizDataInitThread(Integer pageSize, SyncService syncService, BizDataMapper bizDataMapper) {
        super(5, syncService);
        this.bizDataMapper = bizDataMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_BIZ_DATA");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("BotBizData already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BizData> bizDataList) {
        if (CollectionUtils.isEmpty(bizDataList)) {
            return true;
        }
        if (syncService.insertBotBizDataAndCheckListSize(bizDataList, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BizData> bizDataList) {
        if (bizDataList.size() > 0) {
            Date createDate = bizDataList.get(bizDataList.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkBizDataTimeEquals(bizDataList)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_BIZ_DATA");
        }
    }

}
