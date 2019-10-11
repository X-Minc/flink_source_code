/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.canal.common;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ifugle.rap.elasticsearch.enums.ChannelType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.impl.ClusterCanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionBegin;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionEnd;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessApi;

/***
 * 测试基类
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: AbstractCanalClient.java, v 0.1 2018年5月16日 下午1:49:56 HuangLei(wenyuan) Exp $
 */
public class AbstractCanalClient {


    private static String ip = null;
    protected final static Logger             logger             = LoggerFactory
                                                                     .getLogger(AbstractCanalClient.class);
    protected static final String             SEP                = SystemUtils.LINE_SEPARATOR;
    protected static final String             DATE_FORMAT        = "yyyy-MM-dd HH:mm:ss";
    protected volatile boolean                running            = false;
    private volatile boolean                  waiting            = true;
    protected Thread.UncaughtExceptionHandler handler            = new Thread.UncaughtExceptionHandler() {

                                                                     public void uncaughtException(Thread t,
                                                                                                   Throwable e) {
                                                                         logger
                                                                             .error(
                                                                                 "parse events has an error",
                                                                                 e);
                                                                     }
                                                                 };
    protected Thread                          thread             = null;
    protected static String                   context_format     = null;
    protected static String                   row_format         = null;
    protected static String                   transaction_format = null;

    protected String                          destination        = "example";

    @Autowired
    private ElasticSearchBusinessApi          elasticSearchBusinessApi;

    static {
        context_format = SEP + "****************************************************" + SEP;
        context_format += "* Batch Id: [{}] ,count : [{}] , memsize : [{}] , Time : {}" + SEP;
        context_format += "* Start : [{}] " + SEP;
        context_format += "* End : [{}] " + SEP;
        context_format += "****************************************************" + SEP;

        row_format = SEP
                     + "----------------> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {}({}) , gtid : ({}) , delay : {} ms"
                     + SEP;

        transaction_format = SEP
                             + "================> binlog[{}:{}] , executeTime : {}({}) , gtid : ({}) , delay : {}ms"
                             + SEP;

    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    protected void start() {

        //ip = "47.98.48.157";
        System.out.println(" canal IP ===================="+ip);
        System.out.println("==========================start==============================");
        final CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(
            ip, 11111), destination, "", "");

        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable() {

            public void run() {
                process(connector);
            }
        });

        thread.setUncaughtExceptionHandler(handler);
        running = true;
        thread.start();
    }

    protected void stop(CanalConnector connector) {
        if (!running) {
        }
        running = false;
        if (waiting) {
            if (connector instanceof ClusterCanalConnector) {
                ((ClusterCanalConnector) connector).setRetryTimes(-1);
            }
            thread.interrupt();
        }
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }

        MDC.remove("destination");
    }

    protected void process(CanalConnector connector) {
        int batchSize = 5 * 1024;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe();
                waiting = false;
                while (running) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        // try {
                        // Thread.sleep(1000);
                        // } catch (InterruptedException e) {
                        // }
                    } else {
                        printSummary(message, batchId, size);
                        printEntry(message.getEntries());
                    }

                    connector.ack(batchId); // 提交确认
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    private void printSummary(Message message, long batchId, int size) {
        long memsize = 0;
        for (Entry entry : message.getEntries()) {
            memsize += entry.getHeader().getEventLength();
        }

        String startPosition = null;
        String endPosition = null;
        if (!CollectionUtils.isEmpty(message.getEntries())) {
            startPosition = buildPositionForDump(message.getEntries().get(0));
            endPosition = buildPositionForDump(message.getEntries().get(
                message.getEntries().size() - 1));
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        logger.info(context_format,
            new Object[] { batchId, size, memsize, format.format(new Date()), startPosition,
                    endPosition });
    }

    protected String buildPositionForDump(Entry entry) {
        long time = entry.getHeader().getExecuteTime();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String position = entry.getHeader().getLogfileName() + ":"
                          + entry.getHeader().getLogfileOffset() + ":"
                          + entry.getHeader().getExecuteTime() + "(" + format.format(date) + ")";
        if (StringUtils.isNotEmpty(entry.getHeader().getGtid())) {
            position += " gtid(" + entry.getHeader().getGtid() + ")";
        }
        return position;
    }

    protected void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = new Date().getTime() - executeTime;
            Date date = new Date(entry.getHeader().getExecuteTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
                || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN) {
                    TransactionBegin begin = null;
                    try {
                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:"
                                                   + entry.toString(), e);
                    }
                    // 打印事务头信息，执行的线程id，事务耗时
                    logger.info(
                        transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()),
                                simpleDateFormat.format(date), entry.getHeader().getGtid(),
                                String.valueOf(delayTime) });
                    logger.info(" BEGIN ----> Thread id: {}", begin.getThreadId());
                } else if (entry.getEntryType() == EntryType.TRANSACTIONEND) {
                    TransactionEnd end = null;
                    try {
                        end = TransactionEnd.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:"
                                                   + entry.toString(), e);
                    }
                    // 打印事务提交信息，事务id
                    logger.info("----------------\n");
                    logger.info(" END ----> transaction id: {}", end.getTransactionId());
                    logger.info(
                        transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()),
                                simpleDateFormat.format(date), entry.getHeader().getGtid(),
                                String.valueOf(delayTime) });
                }

                continue;
            }

            if (entry.getEntryType() == EntryType.ROWDATA) {
                RowChange rowChage = null;
                try {
                    rowChage = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:"
                                               + entry.toString(), e);
                }

                EventType eventType = rowChage.getEventType();
                //同步的表名
                String tableName = entry.getHeader().getTableName();
                logger.info(
                    row_format,
                    new Object[] { entry.getHeader().getLogfileName(),
                            String.valueOf(entry.getHeader().getLogfileOffset()),
                            entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                            eventType, String.valueOf(entry.getHeader().getExecuteTime()),
                            simpleDateFormat.format(date), entry.getHeader().getGtid(),
                            String.valueOf(delayTime) });

                if (eventType == EventType.QUERY || rowChage.getIsDdl()) {
                    logger.info(" sql ----> " + rowChage.getSql() + SEP);
                    continue;
                }

                for (RowData rowData : rowChage.getRowDatasList()) {
                    if (eventType == EventType.DELETE) {
                        exportMysql2ElasticSearch(tableName, rowData.getBeforeColumnsList());
                    } else if (eventType == EventType.INSERT) {
                        exportMysql2ElasticSearch(tableName, rowData.getAfterColumnsList());
                    } else {
                        exportMysql2ElasticSearch(tableName, rowData.getAfterColumnsList());
                    }
                }
            }
        }
    }

    /***
     * 此处处理插入elasticSearch
     * 
     * @param tableName
     * @param columns
     */
    protected void exportMysql2ElasticSearch(String tableName, List<Column> columns) {
        //处理新增的列的数据
        List<String> catelogNames = new ArrayList<String>(5);
        catelogNames.add("BOT_UNAWARE_DETAIL");
        catelogNames.add("BOT_TRACK_DETAIL");
        catelogNames.add("BOT_CHAT_RESPONSE_MESSAGE");
        catelogNames.add("KBS_QUESTION");
        catelogNames.add("KBS_QUESTION_ARTICLE");
        //String catelogName = "bot_track_detail";
        List<String> columnNames = new ArrayList<String>(60);
        columnNames.add("ID");
        columnNames.add("NODE_ID");
        columnNames.add("SERVER_ID");
        columnNames.add("SERVER_NAME");
        columnNames.add("USER_ID");
        columnNames.add("REQUEST_ID");
        columnNames.add("AUTH_VENDOR");
        columnNames.add("MESSAGE_ID");
        columnNames.add("UTTERANCE");
        columnNames.add("TYPE");
        columnNames.add("SATISFACTION_LEVEL");
        columnNames.add("CATEGORY");
        columnNames.add("PRIORITY");
        columnNames.add("CLUSTER_ID");
        columnNames.add("USER_NAME");
        columnNames.add("SESSION_ID");
        columnNames.add("TRACK_CODE");
        columnNames.add("TRACK_DATA");
        columnNames.add("REMARK");
        columnNames.add("CREATION_DATE");
        columnNames.add("RESPONSE_ID");
        columnNames.add("MESSAGE_TIP");
        columnNames.add("ARRAY_INDEX");
        columnNames.add("SUB_INDEX");
        columnNames.add("TYPE");
        columnNames.add("TITLE");
        columnNames.add("SUMMARY");
        columnNames.add("CONTENT");
        columnNames.add("KNOWLEDGE_ID");
        columnNames.add("ANSWER_SOURCE");
        columnNames.add("QUESTION");
        columnNames.add("GRADE");
        columnNames.add("PRIMARY_KEYWORD");
        columnNames.add("ALTERNATE_KEYWORD");
        columnNames.add("SEARCH_KEYWORD");
        columnNames.add("KEYWORD_OPTION");
        columnNames.add("SYNONYMS");
        columnNames.add("ANSWER");
        columnNames.add("STATUS");
        columnNames.add("ORIGINAL");
        columnNames.add("MAP_SOURCE");
        columnNames.add("QUESTION_ID");
        columnNames.add("ARTICLE_ID");
        columnNames.add("ARTICLE_TYPE");
        columnNames.add("ARTICLE_TITLE");
        columnNames.add("ARTICLE_BLOCK");
        columnNames.add("MAP_ID");
        columnNames.add("SYNC_FLAG");
        columnNames.add("SYNC_TIME");
        columnNames.add("CREATOR");
        for(String catelogName : catelogNames){
            if (StringUtils.equals(tableName.toLowerCase(), catelogName.toLowerCase())) {
                Map<String, Object> map = new HashMap<String, Object>();

                for (Column column : columns) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(column.getName() + " : " + column.getValue());
                    builder.append("    type=" + column.getMysqlType());
                    if (column.getUpdated()) {
                        builder.append("    update=" + column.getUpdated());
                    }
                    builder.append(SEP);
                    logger.info("[AbstractCanalClient] export mysql to elastic " + builder.toString());
                    /*if(columnNames.contains(column.getName().toUpperCase())){
                        map.put(column.getName(),column.getValue());
                    }*/
                    map.put(column.getName(),column.getValue());
                    /*if (StringUtils.equals(column.getName(), "id")) {
                        map.put("id", column.getValue());
                    } else if (StringUtils.equals(column.getName(), "user_name")) {
                        map.put("user_name", column.getValue());
                    } else if (StringUtils.equals(column.getName(), "server_name")) {
                        map.put("server_name", column.getValue());
                    } else if (StringUtils.equals(column.getName(), "track_data")) {
                        map.put("track_data", column.getValue());
                    } else if (StringUtils.equals(column.getName(), "creation_date")) {
                        map.put("creation_date", column.getValue());
                    }*/
                }
                DataRequest request = new DataRequest();
                request.setCatalogType(catelogName);
                request.setMap(map);
                elasticSearchBusinessApi.exportDataMysqlToEs(ChannelType.SHUIXIAOMI.getCode(),request);
            }
        }
    }
}
