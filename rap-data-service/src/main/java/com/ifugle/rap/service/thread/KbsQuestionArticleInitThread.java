/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsQuestionArticleDOMapper;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionArticleDO;
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
public class KbsQuestionArticleInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KbsQuestionArticleInitThread.class);

    private KbsQuestionArticleDOMapper kbsQuestionArticleDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<KbsQuestionArticleDO> kbsQuestionArticleDOS = new ArrayList<KbsQuestionArticleDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                kbsQuestionArticleDOS = kbsQuestionArticleDOMapper.selectKbsQuestionArticleForInit(first, pageSize);
                if (checkExit(kbsQuestionArticleDOS)) {
                    break;
                }
                Thread.sleep(1000);
                pageIndex++;
            }
            writeLocalTime(kbsQuestionArticleDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsQuestionArticleInitThread sleep error {0}", ex));
        }
    }

    public KbsQuestionArticleInitThread(Integer pageSize, SyncService syncService, KbsQuestionArticleDOMapper kbsQuestionArticleDOMapper) {
        super(pageSize, syncService);
        this.kbsQuestionArticleDOMapper = kbsQuestionArticleDOMapper;
    }

    private boolean checkExist() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE");
        if (!StringUtils.isEmpty(lastUpdateTime)) {
            logger.info("KBS_QUESTION_ARTICLE is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<KbsQuestionArticleDO> kbsQuestionArticleDOS) {
        if (CollectionUtils.isEmpty(kbsQuestionArticleDOS)) {
            return true;
        }
        if (syncService.insertKbsQuestionArticleAndCheckListSize(kbsQuestionArticleDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<KbsQuestionArticleDO> kbsQuestionArticleDOS) {
        if (kbsQuestionArticleDOS.size() > 0) {
            Date createDate = kbsQuestionArticleDOS.get(kbsQuestionArticleDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkKbsQuestionArticleTimeEquals(kbsQuestionArticleDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "KBS_QUESTION_ARTICLE");
        }
    }
}
