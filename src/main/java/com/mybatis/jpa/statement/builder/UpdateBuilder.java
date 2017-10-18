package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

import com.mybatis.jpa.meta.MybatisColumnMeta;
import com.mybatis.jpa.meta.PersistentMeta;
import com.mybatis.jpa.statement.MybatisStatementResolver;
import com.mybatis.jpa.statement.SqlAssistant;

public class UpdateBuilder extends AbstractStatementBuilder {

	@Override
	public void parseStatementInternal(MybatisStatementResolver resolver, Method method) {
		// 方法名
		resolver.setMethodName(method.getName());
		// 参数类型
		resolver.setParameterTypeClass(Object.class);
		// sqlScript
		resolver.setSqlScript(buildSql(method));
		// 返回值类型
		resolver.setResultType(int.class);
		resolver.setResultMapId(null);

		resolver.setSqlCommandType(SqlCommandType.UPDATE);

		// 主键策略
		resolver.setKeyGenerator(new NoKeyGenerator());

		resolver.resolve();
	}

	@Override
	protected String buildSqlInternal(Method method, final PersistentMeta persistentMeta) {
		return new SQL() {
			{
				UPDATE(persistentMeta.getTableName());
				for (MybatisColumnMeta columnMeta : persistentMeta.getColumnMetaMap().values()) {
					SET(columnMeta.getColumnName() + " = " + SqlAssistant.resolveSqlParameter(columnMeta));
				}
			}
		}.toString() + SqlAssistant.buildSingleCondition(method, persistentMeta);
	}

}
