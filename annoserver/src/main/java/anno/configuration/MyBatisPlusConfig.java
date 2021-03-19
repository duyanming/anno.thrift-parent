package anno.configuration;

import anno.thrift.module.EnginStrategy;
import anno.thrift.module.Engine;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@MapperScan("anno.repository")
@ComponentScans(
        value = {
                @ComponentScan(value = "anno.componentservice"),
                @ComponentScan(value = "anno.configuration"),
                @ComponentScan(value = "anno.repository")
        })
@EnableAspectJAutoProxy
@PropertySource(ignoreResourceNotFound = true,
        value = {"classpath:/application.yml"},
        encoding = "utf-8",
        factory = ResourceFactory.class)
public class MyBatisPlusConfig {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

    @Value("${common-mybatis.mapper-locations}")
    private String commonMapperLocation;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${spring.datasource.druid.url}")
    private String dbUrl;

    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.druid.min-idle}")
    private int minIdle;

    @Value("${spring.datasource.druid.max-active}")
    private int maxActive;

    @Value("${spring.datasource.druid.max-wait}")
    private long maxWait;

    @Value("${spring.datasource.druid.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.druid.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.test-on-return}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.filter.stat.log-slow-sql}")
    private boolean logSlowSql;

    @Value("${spring.datasource.druid.filter.stat.slow-sql-millis}")
    private long slowSqlMillis;


    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        try {
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setUrl(dbUrl);
            druidDataSource.setFilters("stat,wall");
            druidDataSource.setInitialSize(initialSize);
            druidDataSource.setMinIdle(minIdle);
            druidDataSource.setMaxActive(maxActive);
            druidDataSource.setMaxWait(maxWait);
            druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            druidDataSource.setUseGlobalDataSourceStat(true);
            druidDataSource.setDriverClassName(driverClassName);
            druidDataSource.setValidationQuery(validationQuery);
            druidDataSource.setTestWhileIdle(testWhileIdle);
            druidDataSource.setTestOnBorrow(testOnBorrow);
            druidDataSource.setTestOnReturn(testOnReturn);
            // 设置需要的过滤
            List<Filter> statFilters =new ArrayList<>();
            StatFilter statFilter = new StatFilter();
            statFilter.setLogSlowSql(logSlowSql);
            statFilter.setSlowSqlMillis(slowSqlMillis);
            statFilters.add(statFilter);
            // 设置慢SQL
            druidDataSource.setProxyFilters(statFilters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    @Bean
    public EnginStrategy getMyEnginStrategy() {
        EnginStrategy enginStrategy = new MyEnginStrategy();
        Engine.enginStrategy = enginStrategy;
        return enginStrategy;
    }

    @Bean(name = "sqlSessionFactory")
    public com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean sqlSessionFactory() throws IOException {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(getDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        mybatisSqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));

        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("anno.entities");
        mybatisSqlSessionFactoryBean.setPlugins(getPaginationInterceptor());
        return mybatisSqlSessionFactoryBean;
    }

    @Bean
    public PaginationInterceptor getPaginationInterceptor() {
        return new PaginationInterceptor();
    }
}
