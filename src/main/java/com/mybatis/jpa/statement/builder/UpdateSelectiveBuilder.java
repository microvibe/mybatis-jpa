package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;

import com.mybatis.jpa.meta.MybatisColumnMeta;
import com.mybatis.jpa.meta.PersistentMeta;
import com.mybatis.jpa.statement.MybatisStatementResolver;
import com.mybatis.jpa.statement.SqlAssistant;

public class UpdateSelectiveBuilder extends AbstractStatementBuilder {

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
		// columns
		StringBuilder sets = new StringBuilder();
		sets.append("<trim prefix='' suffix='' suffixOverrides=',' > ");
		for (MybatisColumnMeta columnMeta : persistentMeta.getColumnMetaMap().values()) {

			sets.append("<if test='" + columnMeta.getProperty() + "!= null'> ");
			// columnName = #{ }
			sets.append(columnMeta.getColumnName()).append(" = ").append(SqlAssistant.resolveSqlParameter(columnMeta))
					.append(" , ");
			sets.append("</if> ");
		}

		sets.append("</trim> ");

		return "<script>" + "UPDATE " + persistentMeta.getTableName() + " set " + sets.toString()
				+ SqlAssistant.buildSingleCondition(method, persistentMeta) + "</script>";
	}

}
