# mybatis-jpa
## 1. mybatis-jpa 集成方式
### 1.1 配置文件
```xml
<!-- 在spring-mybatis配置文件中,增加以下配置即可.详见configs/spring-mybatis.xml -->
<!-- Mybatis JPA Mapper 所在包路径 -->
    <bean class="com.mybatis.jpa.core.PersistentEnhancerScaner">
        <property name="mapperPackage" value="com.svili.mapper" />
        <property name="entityPackage" value="com.ybg.model" />
        <property name="sqlSessionFactory" ref="sqlSessionFactory" />
    </bean>
```
### 1.2 Entity示例
```Java
@Entity
/* {@Table}非必须,若无此注解,或其name="",将类名解析为下划线风格 做为表名 */
@Table(name = "user")
public class User {

    /* 非持久化字段 */
    @Transient
    private static final long serialVersionUID = -7788405797990662048L;

    /* {@Id}必须,主键标识,{@Column}非必须,若无此注解,或其name="",将字段名解析为下划线风格 做为SQL列名 */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "password_alias")
    private String password;

    /* {@Enumerated}非必须,若无此注解,按照Mybatis约定,枚举类型使用{@EnumTypeHandler}解析 */
    @Enumerated
    private DataStateEnum state;

    private java.util.Date createTime;
```
### 1.3 mapper示例
```Java
@Repository
@MapperDefinition(domainClass = User.class)
/*entends MybatisBaseMapper非必须,它只是定义了公共的方法签名,便于风格的统一*/
public interface UserMapper extends MybatisBaseMapper<User> {

	String resultMap = ResultMapConstants.DEFAULT_NAMESPACE + ".User";

    /* Like 的通配符需要自行添加 */
    @StatementDefinition
    List<User> selectByUserNameLike(String userName);

    @StatementDefinition
    List<User> selectByUserIdLessThan(Integer userId);

    @StatementDefinition
    List<User> selectByUserIdIsNull();

    /*more condition or complex SQL,need yourself build*/
    
    /**注意,此方法的resultMap是由jpa创建的*/
    @Select("select * from user where user_name = #{userName} and dept_id = #{deptId}")
    @ResultMap(resultMap) 
    List<User> selectComplex(Map<String, Object> args); 
    
    /*build with mapper.xml*/ 
    List<User> selectComplex2(Map<String, Object> args);
```

```xml

	<mapper namespace="com.svili.mapper.UserMapper">
	<resultMap id="BaseResultMap" type="com.svili.model.User">
		<id column="user_id" property="userId" />
		<result column="password_alias" property="password" />
		<result column="state" property="state" typeHandler="org.apache.ibatis.type.EnumOrdinalTypeHandler" />
		<result column="CREATE_TIME" property="createTime" />
	</resultMap>
	<sql id="Base_Column_List">
		USER_ID,  PASSWORD_ALIAS, STATE, CREATE_TIME
	</sql>
	<select id="selectComplex2" parameterType="object" resultMap="BaseResultMap">
		SELECT
		<include refid="Base_Column_List" />
		FROM user
		WHERE user_name = #{userName}
	</select>

</mapper>
```