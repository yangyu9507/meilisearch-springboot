package com.yaron.meilisearch.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/04/03
 * @description MeiliSearch 索引注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MSIndex {

    /**
     * 索引
     */
    String uid() default "";

    /**
     * 主键
     */
    String primaryKey() default "";

    /**
     * 分类最大数量
     */
    int maxValuesPerFacet() default 100;

    /**
     *  单次查询最大数量
     */
    int maxTotalHits() default 1000;
}
