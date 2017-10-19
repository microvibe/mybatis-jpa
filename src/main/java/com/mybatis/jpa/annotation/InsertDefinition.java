package com.mybatis.jpa.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author svili
 * @since 2.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@StatementDefinition
public @interface InsertDefinition {

	Class<?> entity() default void.class;

	boolean selective() default false;

	boolean batch() default false;

}