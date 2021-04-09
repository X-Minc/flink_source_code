/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsQuestionDOMapper;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
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
public class KbsQuestionInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KbsQuestionInitThread.class);

    private KbsQuestionDOMapper kbsQuestionDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<KbsQuestionDO> kbsQuestionDOS = new ArrayList<KbsQuestionDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForInit(first, pageSize);
                if (checkExit(kbsQuestionDOS)) {
                    break;
                }
                Thread.sleep(1000);
                pageIndex++;
            }
            writeLocalTime(kbsQuestionDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsQuestionInitThread sleep error {0}", ex));
        }
    }

    public KbsQuestionInitThread(Integer pageSize, SyncService syncService, KbsQuestionDOMapper kbsQuestionDOMapper) {
        super(pageSize, syncService);
        this.kbsQuestionDOMapper = kbsQuestionDOMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("KBS_QUESTION is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<KbsQuestionDO> kbsQuestionDOS) {
        if (CollectionUtils.isEmpty(kbsQuestionDOS)) {
            return true;
        }
        if (syncService.insertKbsQuestionAndCheckListSize(kbsQuestionDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<KbsQuestionDO> kbsQuestionDOS) {
        if (kbsQuestionDOS.size() > 0) {
            Date createDate = kbsQuestionDOS.get(kbsQuestionDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkQuestionTimeEquals(kbsQuestionDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "KBS_QUESTION");
        }
    }
}
