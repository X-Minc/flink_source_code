package com.ifugle.rap.sqltransform.base;

import com.ifugle.rap.sqltransform.baseenum.KeyWord;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlTask;

import java.util.*;

/**
 * @author Minc
 * @date 2022/1/10 9:40
 */
public abstract class BaseSqlTaskScheduleFactory {
    protected Queue<TransformBase<String>> baseTransforms = new LinkedList<>();
    protected Queue<List<SqlTask>> sqlTaskQueue = new LinkedList<>();


    @SafeVarargs
    public final void addSqlTasks(List<SqlTask>... sqlLists) {
        sqlTaskQueue.addAll(Arrays.asList(sqlLists));
    }

    public final void addSqlTasks(List<SqlTask> sqlList) {
        sqlTaskQueue.add(sqlList);
    }


    /**
     * 将多个匹配操作符加入匹配队列
     *
     * @param transformBases 匹配操作符队列
     */
    @SafeVarargs
    public final void addTransforms(TransformBase<String>... transformBases) throws Exception {
        baseTransforms.addAll(Arrays.asList(transformBases));
    }

    /**
     * 将单个匹配操作符加入匹配队列
     *
     * @param transformBases 匹配操作符
     */
    public void addTransforms(TransformBase<String> transformBases) throws Exception {
        baseTransforms.add(transformBases);
    }

    /**
     * 删除某种关键字匹配规则
     *
     * @param keyWord 要删除的关键字匹配规则
     * @return 删除已存在为true，不存在为false
     */
    public boolean remove(KeyWord keyWord) throws Exception {
        return baseTransforms.removeIf(next -> next.getOperate_name().equals(keyWord));
    }

    /**
     * 查询并获得结果
     *
     * @param sqlTasks       任务列表
     * @param transformBases 匹配规则
     * @return key：任务类型 1.天任务 2.30天任务 3.月任务 value 返回结果的集合
     */
    public abstract Map<Integer, List<IndexDayModel>> doSearchAndGainResult(SqlTask sqlTasks, Queue<TransformBase<String>> transformBases) throws Exception;

    /**
     * 处理结果
     *
     * @param map 任务类型 1.天任务 2.30天任务 3.月任务 value 返回结果的集合
     */
    public abstract void dealWithResult(Map<Integer, List<IndexDayModel>> map) throws Exception;


    //运行
    public void runAllTask() throws Exception {
        List<SqlTask> sqlTasks;
        while ((sqlTasks = sqlTaskQueue.poll()) != null) {
            try {
                for (SqlTask sqlTask : sqlTasks) {
                    Map<Integer, List<IndexDayModel>> integerListMap = doSearchAndGainResult(sqlTask, baseTransforms);
                    dealWithResult(integerListMap);
                }
            } catch (Exception e) {
                throw new Exception("执行任务" + sqlTasks + "时，发生错误", e);
            }
        }
    }
}
