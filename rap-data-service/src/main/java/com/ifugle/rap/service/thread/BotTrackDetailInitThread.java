/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BotTrackDetailDOMapper;
import com.ifugle.rap.model.shuixiaomi.BotTrackDetailDO;
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
public class BotTrackDetailInitThread extends BaseInitThread implements Runnable {

    private BotTrackDetailDOMapper botTrackDetailDOMapper;

    private static final Logger logger = LoggerFactory.getLogger(BotTrackDetailInitThread.class);

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<BotTrackDetailDO> botTrackDetailDOS = new ArrayList<BotTrackDetailDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                botTrackDetailDOS = botTrackDetailDOMapper.selectBotTrackDetailForInit(first, pageSize);
                if (checkExit(botTrackDetailDOS)) {
                    break;
                }
                Thread.sleep(1000);
                pageIndex++;
            }
            writeLocalTime(botTrackDetailDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("BotTrackDetailInitThread sleep error {0}", ex));
        }
    }

    public BotTrackDetailInitThread(Integer pageSize, SyncService syncService, BotTrackDetailDOMapper botTrackDetailDOMapper) {
        super(pageSize, syncService);
        this.botTrackDetailDOMapper = botTrackDetailDOMapper;
    }

    private boolean checkExist() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("BOT_TRACK_DETAIL");
        if (!StringUtils.isEmpty(lastUpdateTime)) {
            logger.info("BOT_TRACK_DETAIL is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BotTrackDetailDO> botTrackDetailDOS) {
        if (CollectionUtils.isEmpty(botTrackDetailDOS)) {
            return true;
        }
        if (syncService.insertBotTrackDetailAndCheckListSize(botTrackDetailDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BotTrackDetailDO> botTrackDetailDOS) {
        if (botTrackDetailDOS.size() > 0) {
            Date createDate = botTrackDetailDOS.get(botTrackDetailDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkBotTrackDetailTimeEquals(botTrackDetailDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_TRACK_DETAIL");
        }
    }
}
