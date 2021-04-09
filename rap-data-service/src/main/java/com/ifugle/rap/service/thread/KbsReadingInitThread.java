/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsReadingDOMapper;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
public class KbsReadingInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KbsReadingInitThread.class);

    private KbsReadingDOMapper kbsReadingDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<KbsReadingDOWithBLOBs> kbsReadingDOS = new ArrayList<KbsReadingDOWithBLOBs>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                kbsReadingDOS = kbsReadingDOMapper.selectKbsReadingForInit(first, pageSize);
                if (checkExit(kbsReadingDOS)) {
                    break;
                }
                pageIndex++;
                Thread.sleep(1000);
            }
            writeLocalTime(kbsReadingDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsReadingInitThread sleep error {0}", ex));
        }
    }

    public KbsReadingInitThread(Integer pageSize, SyncService syncService, KbsReadingDOMapper kbsReadingDOMapper) {
        super(pageSize, syncService);
        this.kbsReadingDOMapper = kbsReadingDOMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_READING");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("KBS_READING is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<KbsReadingDOWithBLOBs> kbsReadingDOS) {
        if (CollectionUtils.isEmpty(kbsReadingDOS)) {
            return true;
        }
        if (syncService.insertKbsReadingAndCheckListSize(kbsReadingDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<KbsReadingDOWithBLOBs> kbsReadingDOS) {
        if (kbsReadingDOS.size() > 0) {
            Date createDate = kbsReadingDOS.get(kbsReadingDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkKbsReadingTimeEquals(kbsReadingDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "KBS_READING");
        }
    }
}
