/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.YhzxxnzzcyDOMapper;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
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
public class YhzxxnzzcyInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(YhzxxnzzcyInitThread.class);

    private YhzxxnzzcyDOMapper yhzxxnzzcyDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<YhzxxnzzcyDO> yhzxxnzzcyDOS = new ArrayList<YhzxxnzzcyDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                yhzxxnzzcyDOS = yhzxxnzzcyDOMapper.selectYhzxxnzzcyForInit(first, pageSize);
                if (checkExit(yhzxxnzzcyDOS)) {
                    break;
                }
                pageIndex++;
                Thread.sleep(1000);
            }
            writeLocalTime(yhzxxnzzcyDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("YhzxxnzzcyInitThread sleep error {0}", ex));
        }
    }

    public YhzxxnzzcyInitThread(Integer pageSize, SyncService syncService, YhzxxnzzcyDOMapper yhzxxnzzcyDOMapper) {
        super(pageSize, syncService);
        this.yhzxxnzzcyDOMapper = yhzxxnzzcyDOMapper;
    }

    private boolean checkExist() {
        logger.info("Yhzxxnzzcy start init");
        String lastCreateTime = CommonUtils.readlocalTimeFile("yhzx_xnzz_cy");
        if (StringUtils.isNotBlank(lastCreateTime)) {
            logger.info("yhzx_xnzz_cy is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<YhzxxnzzcyDO> yhzxxnzzcyDOS) {
        if (CollectionUtils.isEmpty(yhzxxnzzcyDOS)) {
            return true;
        }
        if (syncService.insertYhzxxnzzcyAndCheckListSize(yhzxxnzzcyDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<YhzxxnzzcyDO> yhzxxnzzcyDOS) {
        if (yhzxxnzzcyDOS.size() > 0) {
            Date createDate = yhzxxnzzcyDOS.get(yhzxxnzzcyDOS.size() - 1).getCjsj();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "yhzx_xnzz_cy");
        }
    }
}
