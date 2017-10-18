package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;

import com.mybatis.jpa.annotation.InsertDefinition;
import com.mybatis.jpa.meta.MybatisColumnMeta;
import com.mybatis.jpa.meta.PersistentMeta;
import com.mybatis.jpa.statement.MybatisStatementResolver;
import com.mybatis.jpa.statement.SqlAssistant;

public class InsertBatchBuilder extends AbstractStatementBuilder {

	public boolean matched(Method method) {
		if (method.isAnnotationPresent(InsertDefinition.class)) {
			return !method.getAnnotation(InsertDefinition.class).selective();
		}
		return false;
	}

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

		resolver.setSqlCommandType(SqlCommandType.INSERT);

		// 主键策略
		resolver.setKeyGenerator(new NoKeyGenerator());
		resolver.setKeyProperty(null);
		resolver.setKeyColumn(null);

		resolver.resolve();
	}

	@Override
	protected String buildSqlInternal(Method method, final PersistentMeta persistentMeta) {
		StringBuilder values = new StringBuilder();

		for (MybatisColumnMeta columnMeta : persistentMeta.getColumnMetaMap().values()) {
			if (values.length() > 0) {
				values.append(",");
			}
			values.append(SqlAssistant.resolveSqlParameter(columnMeta, "rowData"));
		}

		return "<script>" + " INSERT INTO " + persistentMeta.getTableName() + " (" + persistentMeta.getColumnNames()
				+ " ) " + " VALUES " + "<foreach item='rowData' index='rowIndex' collection='list' separator=','>"
				+ "( " + values.toString() + " )" + "</foreach>" + "</script>";
	}

}
