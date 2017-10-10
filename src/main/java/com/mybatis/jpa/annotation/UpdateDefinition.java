package com.mybatis.jpa.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/***
 * update all or not null columns from table.
 * <p>
 * SQL like :
 * <li>UPDATE table SET column1 = #{column1},column2 = #{column2} where ...
 * <p>
 * selective SQL like :
 * <li>UPDATE table <trim prefix='' suffix='' suffixOverrides=',' >
 * <li><if test="column1!=null"> SET column1=#{column1}, <//if>
 * <li><//trim> where ...
 * 
 * @author svili
 * @since 2.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@StatementDefinition
public @interface UpdateDefinition {

	boolean selective() default false;

	String where() default "";

}
