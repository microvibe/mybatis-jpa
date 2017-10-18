package com.mybatis.jpa.statement;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;

/**
 * mybatis statement adapter</br>
 * 调用parseStatement()方法,创建/注册statement对象</br>
 * 
 * @author svili
 *
 */
public class MybatisStatementResolver {

	/** mybatis assistant */
	private MapperBuilderAssistant assistant;

	private LanguageDriver languageDriver;

	private String methodName;
	private Class<?> parameterTypeClass;

	/** sql表达式,动态sql需使用<script>标签装饰:<script>dynamicSql</script> */
	private String sqlScript;

	private Integer fetchSize;
	private Integer timeout;

	private StatementType statementType;
	private ResultSetType resultSetType;

	/** insert / update / delete /select */
	private SqlCommandType sqlCommandType;

	/** 主键策略 */
	private KeyGenerator keyGenerator;
	private String keyProperty;
	private String keyColumn;

	/** 方法返回值类型 */
	private Class<?> resultType;
	private String resultMapId;

	public MybatisStatementResolver(MapperBuilderAssistant assistant) {
		this.assistant = assistant;

		/** 初始化默认参数 **/
		this.languageDriver = assistant.getLanguageDriver(null);
		// dynamic & has parameters
		this.statementType = StatementType.PREPARED;
		// unknow
		this.resultSetType = ResultSetType.FORWARD_ONLY;
		// 无主键策略
		// this.keyGenerator = new NoKeyGenerator();
	}

	/** 创建mybatis statement,并向configuration中注册 */
	public final void resolve() {
		SqlSource sqlSource = this.buildSqlSource(sqlScript, parameterTypeClass);
		if (sqlSource != null) {

			// Options options = null;
			final String mappedStatementId = this.getMappedStatementId();

			boolean isSelect = SqlCommandType.SELECT.equals(sqlCommandType);
			boolean flushCache = !isSelect;
			boolean useCache = isSelect;

			assistant.addMappedStatement(mappedStatementId, sqlSource, statementType, sqlCommandType, fetchSize,
					timeout,
					// ParameterMapID
					null, parameterTypeClass, resultMapId, resultType, resultSetType, flushCache, useCache,
					// TODO gcode issue #577
					false, keyGenerator, keyProperty, keyColumn,
					// DatabaseID
					null, languageDriver,
					// ResultSets
					null);
		}
	}

	/**
	 * 调用此方法前,需设置assistant.currentNamespace 和 methodName</br>
	 * 若以上两个参数有一个为空(null or empty),则throw RunTimeException
	 * 
	 * @return currentNamespace + "." + methodName
	 */
	private String getMappedStatementId() {
		String currentNamespace = this.assistant.getCurrentNamespace();

		if (currentNamespace == null || currentNamespace.trim().equals("")) {
			// throw
		}

		if (this.methodName == null || this.methodName.trim().equals("")) {
			// throw
		}

		return currentNamespace + "." + this.methodName;
	}

	/** 创建mybatis SqlSource */
	private SqlSource buildSqlSource(String sqlScript, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
		return languageDriver.createSqlSource(this.assistant.getConfiguration(), sqlScript, parameterTypeClass);
	}

	/** 创建mybatis SqlSource */
	private SqlSource buildSqlSource(String sqlScript, Class<?> parameterTypeClass) {
		return buildSqlSource(sqlScript, parameterTypeClass, this.languageDriver);
	}

	// getter and setter

	public MapperBuilderAssistant getAssistant() {
		return assistant;
	}

	public void setAssistant(MapperBuilderAssistant assistant) {
		this.assistant = assistant;
	}

	public LanguageDriver getLanguageDriver() {
		return languageDriver;
	}

	public void setLanguageDriver(LanguageDriver languageDriver) {
		this.languageDriver = languageDriver;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?> getParameterTypeClass() {
		return parameterTypeClass;
	}

	public void setParameterTypeClass(Class<?> parameterTypeClass) {
		this.parameterTypeClass = parameterTypeClass;
	}

	public String getSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}

	public Integer getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(Integer fetchSize) {
		this.fetchSize = fetchSize;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}

	public ResultSetType getResultSetType() {
		return resultSetType;
	}

	public void setResultSetType(ResultSetType resultSetType) {
		this.resultSetType = resultSetType;
	}

	public SqlCommandType getSqlCommandType() {
		return sqlCommandType;
	}

	public void setSqlCommandType(SqlCommandType sqlCommandType) {
		this.sqlCommandType = sqlCommandType;
	}

	public KeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public String getResultMapId() {
		return resultMapId;
	}

	public void setResultMapId(String resultMapId) {
		this.resultMapId = resultMapId;
	}

	public Class<?> getResultType() {
		return resultType;
	}

	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

}
