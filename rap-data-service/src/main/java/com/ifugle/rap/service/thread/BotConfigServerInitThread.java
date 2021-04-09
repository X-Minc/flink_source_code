/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BotConfigServerMapper;
import com.ifugle.rap.model.shuixiaomi.BotConfigServer;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2019年02月27日 09:52
 */
public class BotConfigServerInitThread extends BaseInitThread implements Runnable {

    private BotConfigServerMapper botConfigServerMapper;

    private static final Logger logger = LoggerFactory.getLogger(BotConfigServerInitThread.class);

    public BotConfigServerInitThread(Integer pageSize, SyncService syncService, BotConfigServerMapper botConfigServerMapper) {
        super(pageSize, syncService);
        this.botConfigServerMapper = botConfigServerMapper;
    }

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        List<BotConfigServer> configServers;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            try {
                configServers = botConfigServerMapper.selectBotConfigServerForInit(first, pageSize);
                logger.info("[BotConfigServerInitThread] thread operate database size = {}", configServers.size());
                if (checkExit(configServers)) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error(MessageFormat.format("BotConfigServerInitThread sleep error : {0}", ex.getCause()), ex);
            }
            pageIndex++;
        }
        writeLocalTime(configServers);
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CONFIG_SERVER");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("BOT_CONFIG_SERVER already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BotConfigServer> botConfigServers) {
        if (CollectionUtils.isEmpty(botConfigServers)) {
            return true;
        }
        if (syncService.insertBotConfigServerAndCheckListSize(botConfigServers, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BotConfigServer> botConfigServers) {
        if (botConfigServers.size() > 0) {
            Date createDate = botConfigServers.get(botConfigServers.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotConfigServerTimeEquals(botConfigServers)) {
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_CONFIG_SERVER");
        }
    }
}