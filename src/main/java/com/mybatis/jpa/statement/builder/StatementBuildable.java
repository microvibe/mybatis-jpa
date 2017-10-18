package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.MapperBuilderAssistant;

/**
 * mybatis statement buider 接口
 * 
 * @author svili
 *
 */
public interface StatementBuildable {

	/***
	 * 创建并注册Mybatis Statement
	 */
	void parseStatement(MapperBuilderAssistant assistant, Method method);

}
