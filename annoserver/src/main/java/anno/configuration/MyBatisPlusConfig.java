package anno.configuration;

import anno.thrift.module.EnginStrategy;
import anno.thrift.module.Engine;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@MapperScan("anno.repository")
public class MyBatisPlusConfig {

    @Bean( name="dataSource")
    public DataSource getDataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/bif?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("bif");
        dataSource.setPassword("123456");
        return dataSource;
    }
    @Bean
    public EnginStrategy getMyEnginStrategy(){
        EnginStrategy enginStrategy=new MyEnginStrategy();
        Engine.enginStrategy=enginStrategy;
        return  enginStrategy;
    }
}
