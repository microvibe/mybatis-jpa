# mybatis-jpa V1.0
## 1. mybatis-jpa 集成方式
### 1.1 配置文件
```xml
<!-- 在spring-mybatis配置文件中,增加以下配置即可.详见configs/spring-mybatis.xml -->
<!-- Mybatis JPA Mapper 所在包路径 -->
    <bean class="com.mybatis.jpa.core.MapperEnhancerScaner">
        <property name="basePackage" value="com.svili.mapper" />
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
    @Column(name = "user_Id")
    private Integer userId;

    @Column(name = "password_alias")
    private String password;

    /* {@Enumerated}非必须,若无此注解,按照Mybatis约定,枚举类型使用{@EnumTypeHandler}解析 */
    @Enumerated
    @Column(name = "state")
    private DataStateEnum state;

    @Column(name = "create_Time")
    private java.util.Date createTime;
```
### 1.3 mapper示例
```Java
@Repository
@MapperDefinition(domainClass = User.class)
/*entends MybatisBaseMapper非必须,它只是定义了公共的方法签名,便于风格的统一*/
public interface UserMapper extends MybatisBaseMapper<User> {

    /* Like 的通配符需要自行添加 */
    @StatementDefinition
    List<User> selectByUserNameLike(String userName);

    @StatementDefinition
    List<User> selectByUserIdLessThan(Integer userId);

    @StatementDefinition
    List<User> selectByUserIdIsNull();

    /*more condition or complex SQL,need yourself build*/
    
    /**注意,此方法的resultMap是jpa自动生成的UserResultMap*/
    @Select("select * from user where user_name = #{userName} and dept_id = #{deptId}")
    @ResultMap(value="UserResultMap") 
    List<User> selectComplex(Map<String, Object> args); /*build with mapper.xml*/ List<User> selectComplex2(Map<String, Object> args);
```
## 2. 示例代码说明
测试代码在test目录</br>
/test/resource 包含了spring + spring mvc + mybaits + jpa 的配置文件样例;<br>
测试代码默认数据库为mysql,如需切换oracle,请在pom.xml中加入oracle ojdbc14依赖;

### 联系方式
QQ交流群:246912326