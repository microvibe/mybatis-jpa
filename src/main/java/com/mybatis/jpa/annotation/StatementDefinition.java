package com.mybatis.jpa.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 赋予method解析权,当前方法将被解析,并在Mybatis Statement中注册</br>
 * 
 * its means that you could not use mapper.xml or Mybatis annotation to build
 * the sql or resultMap,it will be resolved and auto build according to your
 * method sign.so,please definition method with prescribed as follows.</br>
 * 
 * @attation it can only resolve single condition,the condition column must use
 *           fieldName(except primaryKey,because primaryKey will be adapted to
 *           fieldName),and the operate with condition {@see OperateEnum}.
 * @attation if you want to build more or complex condition,you should use
 *           {@link #where()}.
 * @attation when use this annotation in one method,you should ensure that the
 *           class(mapper interface) is annotation with
 *           {@MapperDefinition},otherwise this method can not be resolved.
 * 
 * @author svili
 * @since 2.0
 * @see InsertDefinition
 * @see UpdateDefinition
 * @see InsertSelectiveDefinition
 * @see SelectDefinition
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE })
public @interface StatementDefinition {

	String where() default "";

}
