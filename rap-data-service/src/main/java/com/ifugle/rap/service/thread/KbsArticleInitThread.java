/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsArticleDOMapper;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDOWithBLOBs;
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
public class KbsArticleInitThread extends BaseInitThread implements Runnable {

    private KbsArticleDOMapper kbsArticleDOMapper;

    private static final Logger logger = LoggerFactory.getLogger(KbsArticleInitThread.class);

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        List<KbsArticleDOWithBLOBs> kbsArticleDOS = new ArrayList<KbsArticleDOWithBLOBs>();
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            try {
                kbsArticleDOS = kbsArticleDOMapper.selectKbsArticleForInit(first, pageSize);
                logger.info("[KbsArticleInitThread] thread operate database size = " + kbsArticleDOS.size());
                if (checkExit(kbsArticleDOS)) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error("KbsArticleInitThread sleep error", ex);
            }
            pageIndex++;
        }
        writeLocalTime(kbsArticleDOS);
    }

    public KbsArticleInitThread(Integer pageSize, SyncService syncService, KbsArticleDOMapper kbsArticleDOMapper) {
        super(pageSize, syncService);
        this.kbsArticleDOMapper = kbsArticleDOMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_ARTICLE");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("KBS_ARTICLE already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<KbsArticleDOWithBLOBs> kbsArticleDOS) {
        if (CollectionUtils.isEmpty(kbsArticleDOS)) {
            return true;
        }
        if (syncService.insertKbsArticleAndCheckListSize(kbsArticleDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<KbsArticleDOWithBLOBs> kbsArticleDOS) {
        if (kbsArticleDOS.size() > 0) {
            Date createDate = kbsArticleDOS.get(kbsArticleDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkArticleTimeEquals(kbsArticleDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "KBS_ARTICLE");
        }
    }

    public static void main(String[] args) {
        System.out.println(new CompriseUtils().transportData(new Date().toString()));
    }
}
