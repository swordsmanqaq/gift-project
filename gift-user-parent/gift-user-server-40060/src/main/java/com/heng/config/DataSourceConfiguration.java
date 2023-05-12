package com.heng.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    //mapper.xml路径
    @Value("${mybatis-plus.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis-plus.type-aliases-package}")
    private String typeAliasesPackage;

    //手动配置bean
    @Bean
    @ConfigurationProperties("spring.datasource")
    @Primary
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSourceProxy dataSourceProxy) throws Exception {
        //处理MybatisPlus
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSourceProxy);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        factory.setTypeAliasesPackage(typeAliasesPackage);
        //事务管理工厂
        factory.setTransactionFactory(new SpringManagedTransactionFactory());
        return factory;
    }

    @Bean
    public DataSourceProxy dataSource() {
        return new DataSourceProxy(druidDataSource());
    }
}