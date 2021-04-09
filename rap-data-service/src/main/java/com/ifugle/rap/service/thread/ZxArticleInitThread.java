/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.zhcs.ZxArticleMapper;
import com.ifugle.rap.model.zhcs.ZxArticle;
import com.ifugle.rap.service.SyncService;
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
public class ZxArticleInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ZxArticleInitThread.class);

    private ZxArticleMapper zxArticleMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<ZxArticle> zxArticles = new ArrayList<ZxArticle>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                zxArticles = zxArticleMapper.selectZxArticleForInit(first, pageSize);
                if (checkExit(zxArticles)) {
                    break;
                }
                Thread.sleep(1000);
                pageIndex++;
            }
            writeLocalTime(zxArticles);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsQuestionInitThread sleep error {0}", ex));
        }
    }

    public ZxArticleInitThread(Integer pageSize, SyncService syncService, ZxArticleMapper zxArticleMapper) {
        super(pageSize, syncService);
        this.zxArticleMapper = zxArticleMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("ZX_ARTICLE");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("ZX_ARTICLE is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<ZxArticle> zxArticles) {
        if (CollectionUtils.isEmpty(zxArticles)) {
            return true;
        }
        if (syncService.insertZxArticleAndCheckListSize(zxArticles, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<ZxArticle> zxArticles) {
        if (!CollectionUtils.isEmpty(zxArticles)) {
            Date createDate = zxArticles.get(zxArticles.size() - 1).getCreationDate();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "ZX_ARTICLE");
        }
    }
}
