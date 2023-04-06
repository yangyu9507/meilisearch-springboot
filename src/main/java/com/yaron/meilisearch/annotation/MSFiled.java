package com.yaron.meilisearch.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/04/03
 * @description MeiliSearch 字段注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MSFiled {

    /**
     * 是否开启过滤
     */
    boolean openFilter() default false;

    /**
     * 是否不展示
     */
    boolean noDisplayed() default false;

    /**
     * 是否开启排序
     */
    boolean openSort() default false;


    /**
     *  处理的字段名
     */
    String key();

    /**
     * 字段说明
     */
    String description() default StringUtils.EMPTY;
}
