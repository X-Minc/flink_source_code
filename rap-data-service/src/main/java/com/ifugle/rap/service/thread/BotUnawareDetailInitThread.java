/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BotUnawareDetailDOMapper;
import com.ifugle.rap.model.shuixiaomi.BotUnawareDetailDO;
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
public class BotUnawareDetailInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BotUnawareDetailInitThread.class);

    private BotUnawareDetailDOMapper botUnawareDetailDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            List<BotUnawareDetailDO> botUnawareDetailDOS = new ArrayList<BotUnawareDetailDO>();
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailForInit(first, pageSize);
                if (checkExit(botUnawareDetailDOS)) {
                    break;
                }
                Thread.sleep(1000);
                pageIndex++;
            }
            writeLocalTime(botUnawareDetailDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("BotUnawareDetailInitThread sleep error , {0}", ex));
        }
    }

    public BotUnawareDetailInitThread(Integer pageSize, SyncService syncService, BotUnawareDetailDOMapper botUnawareDetailDOMapper) {
        super(pageSize, syncService);
        this.botUnawareDetailDOMapper = botUnawareDetailDOMapper;
    }

    private boolean checkExist() {
        String createTime = CommonUtils.readlocalTimeFile("BOT_UNAWARE_DETAIL");
        if (!StringUtils.isEmpty(createTime)) {
            logger.info("BOT_UNAWARE_DETAIL already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BotUnawareDetailDO> botUnawareDetailDOS) {
        if (CollectionUtils.isEmpty(botUnawareDetailDOS)) {
            return true;
        }
        if (syncService.insertBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BotUnawareDetailDO> botUnawareDetailDOS) {
        if (botUnawareDetailDOS.size() > 0) {
            Date createDate = botUnawareDetailDOS.get(botUnawareDetailDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkBotUnawareDetailTimeEquals(botUnawareDetailDOS)){
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_UNAWARE_DETAIL");
        }
    }
}
