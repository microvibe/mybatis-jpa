package com.mybatis.jpa.statement.builder;

import java.lang.reflect.Method;

public interface SQLBuildable {

	String buildSql(Method method);

}
