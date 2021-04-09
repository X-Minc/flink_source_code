package com.ifugle.rap.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Jul 2, 2016 5:10:24 PM
 * @version $Id: Label.java 18095 2016-07-04 15:00:29Z WuJianqiang $
 * @author WuJianqiang
 *
 */
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface Label {

    /**
     * The textual label to associate with the program element.
     */
    String value();

}
