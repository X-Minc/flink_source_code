/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsKeywordDOMapper;
import com.ifugle.rap.model.shuixiaomi.KbsKeywordDO;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
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
public class KbsKeywordInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KbsKeywordInitThread.class);

    private KbsKeywordDOMapper kbsKeywordDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<KbsKeywordDO> kbsKeywordDOS = new ArrayList<KbsKeywordDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                kbsKeywordDOS = kbsKeywordDOMapper.selectKbsKeywordForInit(first, pageSize);
                if (checkExit(kbsKeywordDOS)) {
                    break;
                }
                pageIndex++;
                Thread.sleep(1000);
            }
            writeLocalTime(kbsKeywordDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsKeywordInitThread sleep error {0}", ex));
        }

    }

    public KbsKeywordInitThread(Integer pageSize, SyncService syncService, KbsKeywordDOMapper kbsKeywordDOMapper) {
        super(pageSize, syncService);
        this.kbsKeywordDOMapper = kbsKeywordDOMapper;
    }

    private boolean checkExist() {
        logger.info("KbsKeyword start init");
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_KEYWORD");
        if (org.apache.commons.lang.StringUtils.isNotBlank(lastCreateTime)) {
            logger.info("KBS_KEYWORD is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<KbsKeywordDO> kbsKeywordDOS) {
        if (CollectionUtils.isEmpty(kbsKeywordDOS)) {
            return true;
        }
        if (syncService.insertKbsKeywordAndCheckListSize(kbsKeywordDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<KbsKeywordDO> kbsKeywordDOS) {
        if (kbsKeywordDOS.size() > 0) {
            Date createDate = kbsKeywordDOS.get(kbsKeywordDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkKbsKeywordTimeEquals(kbsKeywordDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "KBS_KEYWORD");
        }
    }
}
