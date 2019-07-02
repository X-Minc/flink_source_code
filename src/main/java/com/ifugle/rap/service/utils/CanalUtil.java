/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.utils;

import java.text.MessageFormat;
import java.util.List;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.ifugle.rap.model.enums.DatabaseEnum;
import com.ifugle.rap.model.enums.TablesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiuZhengyang
 * @version $Id: CanalUtil.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月12日 10:49
 */
public class CanalUtil {

    private static final Logger logger = LoggerFactory.getLogger(CanalUtil.class);

    private static Integer i = 1;

    public static void AnalyzeEntry(List<Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {

            //该条信息是事务开始或者结束的记录时，跳过
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            //该条信息的数据库是需要过滤的数据库时，跳过
            if (DatabaseEnum.DATABASE_NAME.contains(entry.getHeader().getSchemaName().toLowerCase())) {
                continue;
            }
            //该条信息的所属表不在枚举里面时,跳过
            if (!TablesEnum.TABLE_NAMES.contains(entry.getHeader().getTableName().toLowerCase())) {
                continue;
            }
            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            CanalEntry.EventType eventType = rowChage.getEventType();
            logger.info(String
            .format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s", entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
            entry.getHeader().getSchemaName(), entry.getHeader().getTableName(), eventType));

            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                logger.info(MessageFormat.format("======================================{0}条记录======================================================", i));
                i++;
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    logger.info("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    logger.info("-------> after");
                    printColumn(rowData.getAfterColumnsList());
                }

            }

        }
    }

    public static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            logger.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
