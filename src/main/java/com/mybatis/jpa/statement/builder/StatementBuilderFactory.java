package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

import com.mybatis.jpa.annotation.InsertDefinition;
import com.mybatis.jpa.annotation.UpdateDefinition;

public class StatementBuilderFactory {

	public static StatementBuildable create(Method method) {
		StatementBuildable builder = null;

		if (method.isAnnotationPresent(UpdateDefinition.class)) {
			UpdateDefinition definition = method.getAnnotation(UpdateDefinition.class);
			if (definition.selective()) {
				builder = new UpdateSelectiveBuilder();
			} else {
				builder = new UpdateBuilder();
			}
		}

		if (method.isAnnotationPresent(InsertDefinition.class)) {
			InsertDefinition definition = method.getAnnotation(InsertDefinition.class);
			boolean batch = definition.batch();
			boolean selective = definition.selective();
			if (batch) {
				return new InsertBatchBuilder();
			}
			if (!batch && selective) {
				builder = new InsertSelectiveBuilder();
			}
			if (!batch && !selective) {
				builder = new InsertBuilder();
			}
		}

		if (builder == null) {
			// throw
		}

		return builder;
	}

}
