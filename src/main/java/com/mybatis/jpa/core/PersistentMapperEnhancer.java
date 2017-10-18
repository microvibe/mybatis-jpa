package com.mybatis.jpa.core;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.annotation.StatementDefinition;
import com.mybatis.jpa.statement.builder.StatementBuildable;
import com.mybatis.jpa.statement.builder.StatementBuilderFactory;

/**
 * Persistent Mapper Enhancer(增强器)</br>
 * 
 * @attation 由Mybatis负责创建Mapper接口的代理</br>
 * @attation 该enhancer只负责:</br>
 *           1.解析Method,创建并注册Mybatis Statement {@see MappedStatement}
 * 
 * @author svili
 *
 */
public class PersistentMapperEnhancer extends BaseBuilder {

	/** mybatis */
	protected MapperBuilderAssistant assistant;

	/** mybatis mapper接口类型 */
	protected Class<?> mapper;

	/**
	 * 容器中configuration唯一,必须初始化
	 * 
	 * @param configuration
	 *            mybatis configuration{@see Configuration}
	 * @param mapper
	 *            mybatis mapper接口类型
	 */
	public PersistentMapperEnhancer(Configuration configuration, Class<?> mapper) {
		super(configuration);
		String resource = mapper.getName().replace(".", "/") + ".java (best guess)";
		this.assistant = new MapperBuilderAssistant(configuration, resource);
		this.mapper = mapper;
	}

	/** mapper增强方法入口 */
	public void enhance() {
		String resource = mapper.toString();
		if (!configuration.isResourceLoaded(resource)) {
			configuration.addLoadedResource(resource);
		}
		assistant.setCurrentNamespace(mapper.getName());
		// no cache

		// mapper will be definition if it has annotation with <code>
		// MapperDefinition</code>
		if (!mapper.isAnnotationPresent(MapperDefinition.class)) {
			return;
		}

		/*
		 * build and register Mybatis Statement {@see MappedStatement},
		 * class.getMethods means ,also contains methods from superClass
		 */
		for (Method method : mapper.getMethods()) {
			/*
			 * method will be definition if it has annotation with <code>
			 * StatementDefinition</code>
			 */
			if (method.isAnnotationPresent(StatementDefinition.class)) {
				StatementBuildable statementBuilder = StatementBuilderFactory.create(method);
				if (statementBuilder != null) {
					statementBuilder.parseStatement(assistant, method);
				}
			}
		}
	}

}
