/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.BotMediaDOMapper;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.security.crypto.CryptZip;
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
public class BotMediaInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BotMediaInitThread.class);

    private CryptZip cryptZip;

    private BotMediaDOMapper mediaDOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        List<BotMediaDO> botMediaDOS = new ArrayList<BotMediaDO>();
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                botMediaDOS = mediaDOMapper.selectBotMediaForInit(first, pageSize);
                //判断是否跳出循环
                if (checkExit(botMediaDOS)) {
                    break;
                }
                pageIndex++;
                Thread.sleep(1000);
            }
            //最后一个的创建时间写入本地文件
            writeLocalTime(botMediaDOS);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("BotMediaInitThread sleep error {0}", ex));
        }
    }

    public BotMediaInitThread(Integer pageSize, SyncService syncService, CryptZip cryptZip, BotMediaDOMapper mediaDOMapper) {
        super(pageSize, syncService);
        this.cryptZip = cryptZip;
        this.mediaDOMapper = mediaDOMapper;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_MEDIA");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.info("BOT_MEDIA is already exists");
            return true;
        }
        return false;
    }

    private boolean checkExit(List<BotMediaDO> botMediaDOS) {
        if (CollectionUtils.isEmpty(botMediaDOS)) {
            return true;
        }
        if (syncService.insertBotMediaAndCheckListSize(botMediaDOS, pageSize)) {
            return true;
        }
        return false;
    }

    private void writeLocalTime(List<BotMediaDO> botMediaDOS) {
        if (botMediaDOS.size() > 0) {
            Date createTime = botMediaDOS.get(botMediaDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if( BizListCheckUtils.checkBotMediaTimeEquals(botMediaDOS)){
                createTime = TimeDelayUtils.getNextMilliDate(createTime);
            }
            CommonUtils.writeLocalTimeFile(createTime.toString(), "BOT_MEDIA");
        }
    }

}
