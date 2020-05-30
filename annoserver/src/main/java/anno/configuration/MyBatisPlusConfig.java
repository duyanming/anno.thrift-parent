package anno.configuration;

import anno.thrift.module.EnginStrategy;
import anno.thrift.module.Engine;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan("anno.repository")
@ComponentScans(
    value = {
      @ComponentScan(value = "anno.componentservice"),
      @ComponentScan(value = "anno.configuration"),
      @ComponentScan(value = "anno.repository")
    })
@EnableAspectJAutoProxy
public class MyBatisPlusConfig {

  @Bean(name = "dataSource")
  public DataSource getDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl(
        "jdbc:mysql://localhost:3306/bif?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8");
    dataSource.setUsername("bif");
    dataSource.setPassword("123456");
    return dataSource;
  }

  @Bean
  public EnginStrategy getMyEnginStrategy() {
    EnginStrategy enginStrategy = new MyEnginStrategy();
    Engine.enginStrategy = enginStrategy;
    return enginStrategy;
  }
    @Bean(name = "sqlSessionFactory")
  public com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean sqlSessionFactory() throws IOException {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean= new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-cfg.xml"));
        mybatisSqlSessionFactoryBean.setDataSource(getDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        mybatisSqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));

        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("anno.entities");
        mybatisSqlSessionFactoryBean.setPlugins(getPaginationInterceptor());
        return mybatisSqlSessionFactoryBean;
  }
  @Bean
  public PaginationInterceptor getPaginationInterceptor(){
      return new PaginationInterceptor();
  }
}
