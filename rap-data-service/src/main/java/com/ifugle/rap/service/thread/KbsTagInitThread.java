/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.thread;

import com.ifugle.rap.mapper.KbsTagDTOMapper;
import com.ifugle.rap.model.shuixiaomi.dto.KbsTagDTO;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author LiuZhengyang
 * @version $Id: KbsTagInitThread.java 84710 2018-11-09 08:19:43Z HuangLei $
 * @since 2018年10月16日 14:48
 */
public class KbsTagInitThread extends BaseInitThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KbsTagInitThread.class);

    @Autowired
    private KbsTagDTOMapper kbsTagDTOMapper;

    public void run() {
        if (checkExist()) {
            return;
        }
        int pageIndex = 1;
        try {
            Map<Long, LinkedList<String>> tags = new HashMap<Long, LinkedList<String>>(20 * 16);
            List<KbsTagDTO> kbsTagDTOS = new ArrayList<KbsTagDTO>(20 * 16);
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<KbsTagDTO> list = kbsTagDTOMapper.selectKbsTagForInit(first, pageSize);
                if (checkExit(list)) {
                    break;
                }
                //将所有的tag组合起来,一个questionId对应全部的tag
                tags = compriseTags(list, tags);
                kbsTagDTOS = list;
                pageIndex++;
                Thread.sleep(1000);
            }
            exec(kbsTagDTOS, tags);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("KbsTagInitThread sleep error {0}", ex));
        }

    }

    public KbsTagInitThread(Integer pageSize, SyncService syncService, KbsTagDTOMapper kbsTagDTOMapper) {
        super(pageSize, syncService);
        this.kbsTagDTOMapper = kbsTagDTOMapper;
    }

    private boolean checkExit(List<KbsTagDTO> kbsTagDTOS) {
        //为空跳出
        if (CollectionUtils.isEmpty(kbsTagDTOS)) {
            return true;
        }
        return false;
    }

    private Map<Long, LinkedList<String>> compriseTags(List<KbsTagDTO> kbsTagDTOS, Map<Long, LinkedList<String>> tags) {
        for (KbsTagDTO tag : kbsTagDTOS) {
            if (tags.containsKey(tag.getLinkId())) {
                LinkedList<String> list = tags.get(tag.getLinkId());
                list.addFirst(tag.getName());
                tags.put(tag.getLinkId(), list);
            } else {
                LinkedList<String> list = new LinkedList<String>();
                list.addFirst(tag.getName());
                tags.put(tag.getLinkId(), list);
            }
        }
        return tags;
    }

    private boolean checkExist() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_TAG");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            logger.warn("BOT_TAG is already exists");
            return true;
        }
        return false;
    }

    private void exec(List<KbsTagDTO> kbsTagDTOS, Map<Long, LinkedList<String>> tags) {
        if (kbsTagDTOS.size() > 0) {
            syncService.insertKbsTags(tags);
            Date createDate = kbsTagDTOS.get(kbsTagDTOS.size() - 1).getCreationDate();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_TAG");
        }
    }

}
