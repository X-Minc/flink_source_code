/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.utils;

import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.shuixiaomi.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author HuangLei(wenyuan)
 * @version $Id BizListCheckUtils.java v 0.1 2019/1/22 HuangLei(wenyuan) Exp $
 */
public class BizListCheckUtils {

    /***
     * 判断question时间是否相同
     * @param questionDOList
     * @return
     */
    public static boolean checkQuestionTimeEquals(List<KbsQuestionDO> questionDOList) {
        if (CollectionUtils.isNotEmpty(questionDOList)) {
            Date time = questionDOList.get(0).getCreationDate();
            for (KbsQuestionDO kbsQuestionDO : questionDOList) {
                if (!TimeDelayUtils.isSameDate(kbsQuestionDO.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkArticleTimeEquals(List<KbsArticleDOWithBLOBs> articleDOList) {
        if (CollectionUtils.isNotEmpty(articleDOList)) {
            Date time = articleDOList.get(0).getCreationDate();
            for (KbsArticleDO kbsArticleDO : articleDOList) {
                if (!TimeDelayUtils.isSameDate(kbsArticleDO.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean checkYhzxXnzzNsrTimeEquals(List<YhzxXnzzNsr> yhzxXnzzNsrs) {
        if (CollectionUtils.isNotEmpty(yhzxXnzzNsrs)) {
            Date time = yhzxXnzzNsrs.get(0).getCjsj();
            for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
                if (!TimeDelayUtils.isSameDate(yhzxXnzzNsr.getCjsj(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBizDataTimeEquals(List<BizData> bizDataList) {
        if (CollectionUtils.isNotEmpty(bizDataList)) {
            Date time = bizDataList.get(0).getCreationDate();
            for (BizData bizData : bizDataList) {
                if (!TimeDelayUtils.isSameDate(bizData.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotConfigServerTimeEquals(List<BotConfigServer> botConfigServers) {
        if (CollectionUtils.isNotEmpty(botConfigServers)) {
            Date time = botConfigServers.get(0).getCreationDate();
            for (BotConfigServer botConfigServer : botConfigServers) {
                if (!TimeDelayUtils.isSameDate(botConfigServer.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotChatResponseMessageTimeEquals(List<BotChatResponseMessageDO> articleDOList) {
        if (CollectionUtils.isNotEmpty(articleDOList)) {
            Date time = articleDOList.get(0).getCreationDate();
            for (BotChatResponseMessageDO kbsArticleDO : articleDOList) {
                if (!TimeDelayUtils.isSameDate(kbsArticleDO.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotOutoundTaskDetailEquals(List<BotOutoundTaskDetailWithBLOBs> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (BotOutoundTaskDetailWithBLOBs model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotMediaTimeEquals(List<BotMediaDO> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (BotMediaDO model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotTrackDetailTimeEquals(List<BotTrackDetailDO> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (BotTrackDetailDO model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBotUnawareDetailTimeEquals(List<BotUnawareDetailDO> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (BotUnawareDetailDO model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkKbsQuestionArticleTimeEquals(List<KbsQuestionArticleDO> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (KbsQuestionArticleDO model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkKbsKeywordTimeEquals(List<KbsKeywordDO> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (KbsKeywordDO model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkKbsReadingTimeEquals(List<KbsReadingDOWithBLOBs> modelList) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            Date time = modelList.get(0).getCreationDate();
            for (KbsReadingDOWithBLOBs model : modelList) {
                if (!TimeDelayUtils.isSameDate(model.getCreationDate(), time)) {
                    return false;
                }
            }
        }
        return true;
    }

}
