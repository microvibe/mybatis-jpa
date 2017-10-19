package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.MapperBuilderAssistant;

import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.meta.PersistentMeta;
import com.mybatis.jpa.statement.MybatisStatementResolver;

public abstract class AbstractStatementBuilder implements StatementBuildable, SQLBuildable {

	@Override
	public void parseStatement(MapperBuilderAssistant assistant, Method method) {
		MybatisStatementResolver resolver = new MybatisStatementResolver(assistant);
		parseStatementInternal(resolver, method);
	}

	protected abstract void parseStatementInternal(MybatisStatementResolver resolver, Method method);

	@Override
	public String buildSql(Method method) {
		Class<?> mapper = method.getDeclaringClass();
		Class<?> entityType = mapper.getAnnotation(MapperDefinition.class).domainClass();
		PersistentMeta persistentMeta = new PersistentMeta(entityType);
		return buildSqlInternal(method, persistentMeta);
	}

	protected abstract String buildSqlInternal(Method method, final PersistentMeta persistentMeta);

}
