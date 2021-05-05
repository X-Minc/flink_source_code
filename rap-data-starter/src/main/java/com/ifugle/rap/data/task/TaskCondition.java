package com.ifugle.rap.data.task;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年08月20日 15:19
 */
public class TaskCondition implements Condition {
    /**
     * 是否启用ES定时任务
     */
    public static String TASK_ENABLE =
            NullUtil.isNull(System.getProperty("rap.sjtj.task.enable")) ? "false" : System.getProperty("rap.sjtj.task.enable");

    /**
     * 是否启用ES定时任务， true则启动， false则不启动
     * @param conditionContext
     * @param annotatedTypeMetadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Boolean.valueOf(TASK_ENABLE);
    }
}
