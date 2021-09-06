package com.ifugle.rap.data.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ifugle.rap.bigdata.task.DepartAggDw;
import com.ifugle.rap.bigdata.task.service.EsDepartAggDwService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.boot.redis.SKOVRedisTemplate;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.service.impl.DepartAggBmIdQueueRedisService;
import com.ifugle.rap.service.impl.DepartAggRedisService;
import com.ifugle.util.NullUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author XuWeigang
 * @since 2019年09月11日 11:01
 */
@Component
@Conditional(TaskCondition.class)
@Slf4j
public class RealTimeThreadTask {
    private static final int POOL_DEFAULT_SIZE = 5;
    private static final String POOL_SIZE = "rap.sjtj.task.pool.size";
    @Autowired
    private DepartAggRedisService departAggRedisService;
    @Autowired
    private DepartAggBmIdQueueRedisService departAggBmIdQueueRedisService;
    @Autowired
    private SKOVRedisTemplate skovRedisTemplate;
    @Autowired
    private EsDepartAggDwService esDepartAggDwService;
    @Autowired
    private EsService esService;
    private ExecutorService threadPoolForBm;
    private DepartAggDwUpdateThread departAggDwUpdateThread;

    /**
     * 执行线程初始化
     */
    @PostConstruct
    public void initialize() {
        // 获取线程数配置，如果为空取默认大小
        String poolSize = System.getProperty(POOL_SIZE);
        int threadPoolSize = POOL_DEFAULT_SIZE;
        if (NullUtil.isNotNull(poolSize)) {
            threadPoolSize = Integer.valueOf(poolSize);
        }

        // 初始化线程池，多线程进行汇总计算
        if (threadPoolForBm == null || threadPoolForBm.isShutdown() || threadPoolForBm.isTerminated()) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("depart-agg-pool-%d").build();
            threadPoolForBm = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>(5), threadFactory,
                    new ThreadPoolExecutor.AbortPolicy());
            for (int i = 0; i < threadPoolSize; i++) {
                threadPoolForBm.execute(new AggregateByBmTask());
            }
            threadPoolForBm.shutdown();
        }

        // 初始化线程，单线程将计算完的数据更新到ES
        if (departAggDwUpdateThread == null || !departAggDwUpdateThread.isAlive()) {
            departAggDwUpdateThread = new DepartAggDwUpdateThread();
            departAggDwUpdateThread.start();
        }
    }

    /**
     * 按部门汇计算线程
     */
    class AggregateByBmTask implements Runnable {
        private boolean isRunning = false;
        private boolean isWaited = false;
        private long timeMillis = 0L;

        @Override
        public void run() {
            log.info("部门汇总线程启动: {}", Thread.currentThread().getName());
            List<Long> bmIds = null;
            // 当队列数量为0且定时任务结束时才结束线程
            while (true) {
                try {
                    ListOperations<String, Object> opsForList = skovRedisTemplate.opsForList();
                    long size = opsForList.size(Thread.currentThread().getName());
                    List<Object> bmIdList = opsForList.range(Thread.currentThread().getName(), 0, size);
                    if (bmIdList == null) {
                        bmIdList = new ArrayList<>();
                    }
                    bmIds = bmIdList.stream().map(bmId -> Long.valueOf((String)bmId)).collect(Collectors.toList());

                    if (NullUtil.isNull(bmIds)) {
                        bmIds = departAggBmIdQueueRedisService.getBmIdsFromQueue(EsCode.BM_BATCH_NUM);
                    }

                    if (NullUtil.isNull(bmIds)) {
                        if (isRunning && isWaited) {
                            long time = (System.currentTimeMillis() - timeMillis) / 1000;
                            log.info("部门汇总线程执行结束: {}, 运行时间：{}s", Thread.currentThread().getName(), time);
                            isRunning = false;
                            isWaited = false;
                        }

                        // 如果没有数据，等待五秒再取
                        try {
                            TimeUnit.SECONDS.sleep(5);
                            isWaited = true;
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!isRunning) {
                        log.info("部门汇总线程执行开始: {}", Thread.currentThread().getName());
                        isRunning = true;
                        isWaited = false;
                        timeMillis = System.currentTimeMillis();
                    }

                    try {
                        esDepartAggDwService.aggregateStatisticsByBmIds(bmIds);
                    } catch (Exception e) {
                        log.error("汇总失败，重新加入队列，bmIds = {}", bmIds);
                        // 失败重新加入队列
                        addToQueue(bmIds);
                    }
                    // 汇总完成后删除
                    skovRedisTemplate.delete(Thread.currentThread().getName());
                } catch (Exception e) {
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }

        private void addToQueue(List<Long> bmIds) {
            departAggBmIdQueueRedisService.addBmIdToQueue(bmIds);
        }
    }

    class DepartAggDwUpdateThread extends Thread {
        private boolean isRunning = false;
        private boolean isWaited = false;
        private long timeMillis = 0L;

        @Override
        public void run() {
            log.info("部门汇总结果更新线程启动: {}", Thread.currentThread().getName());
            while (true) {
                try {
                    Map<String, DepartAggDw> dataMap = departAggRedisService.getDepartAggDwItems(EsCode.ES_PAGE_NUM);
                    if (NullUtil.isNull(dataMap)) {
                        if (isRunning && isWaited) {
                            long time = (System.currentTimeMillis() - timeMillis) / 1000;
                            log.info("部门汇总结果更新线程执行结束: {}, 运行时间：{}s", Thread.currentThread().getName(), time);
                            isRunning = false;
                            isWaited = false;
                        }

                        // 如果没有数据，等待五秒再取
                        try {
                            TimeUnit.SECONDS.sleep(5);
                            isWaited = true;
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (dataMap.size() > 0) {
                        if (!isRunning) {
                            log.info("部门汇总结果更新线程执行开始: {}", Thread.currentThread().getName());
                            isRunning = true;
                            isWaited = false;
                            timeMillis = System.currentTimeMillis();
                        }

                        esService.multiInsertOrUpdate(EsIndexConstant.DEPART_AGG_DW, dataMap);
                        List<String> deleteKeys = new ArrayList<>(dataMap.keySet());
                        // 从redis中删除已更新的数据
                        if (deleteKeys.size() > 0) {
                            departAggRedisService.deleteItems(deleteKeys);
                        }
                        log.info("部门汇总数据更新到ES: count = {}", dataMap.size());
                    }
                } catch (Exception e) {
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }
    }
}
