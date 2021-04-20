package com.ifugle.rap.data.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ifugle
 * @version $
 * @since 4月 12, 2021 17:43
 */
public class AppBotCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String value =  environment.getProperty("is_bot_open");
        if(StringUtils.equals(value,"true")){
            return true;
        }
        return false;
    }
}
