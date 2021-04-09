/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BotChatResponseMessageDOMapper;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
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
public class BotChatResponseMessageInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BotChatResponseMessageInitThread.class);

    private BotChatResponseMessageDOMapper botChatResponseMessageDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<BotChatResponseMessageDO> botChatResponseMessageDOS = new ArrayList<BotChatResponseMessageDO>(20 * 16);
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                botChatResponseMessageDOS = botChatResponseMessageDOMapper.selectBotChatResponseMessageForInit(first, pageSize);
                //判断是否结束
                if (checkExit(botChatResponseMessageDOS)) {
                    break;
                }
                pageIndex++;
                Thread.sleep(1000);
            }
            //将最后一个创建时间写入本地文件
            writeLocalTime(botChatResponseMessageDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("BotChatResponseMessageInitThread sleep error {0}", ex));
        }
    }

    public BotChatResponseMessageInitThread(Integer pageSize, SyncService syncService,
    BotChatResponseMessageDOMapper botChatResponseMessageDOMapper) {
        super(pageSize, syncService);
        this.botChatResponseMessageDOMapper = botChatResponseMessageDOMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_RESPONSE_MESSAGE");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("BOT_CHAT_RESPONSE_MESSAGE is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BotChatResponseMessageDO> botChatResponseMessageDOS) {
        if (CollectionUtils.isEmpty(botChatResponseMessageDOS)) {
            return true;
        }
        if (syncService.insertBotChatResponseMessageAndCheckListSize(botChatResponseMessageDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BotChatResponseMessageDO> botChatResponseMessageDOS) {
        if (botChatResponseMessageDOS.size() > 0) {
            Date createDate = botChatResponseMessageDOS.get(botChatResponseMessageDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkBotChatResponseMessageTimeEquals(botChatResponseMessageDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_CHAT_RESPONSE_MESSAGE");
        }
    }

}
