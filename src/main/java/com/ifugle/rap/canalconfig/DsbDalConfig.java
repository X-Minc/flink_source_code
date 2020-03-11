package com.ifugle.rap.canalconfig;

import javax.sql.DataSource;

import com.ifugle.rap.mapper.dsb.XxzxXxmxMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrBqMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzTpcQyMapper;

@Configuration
@ImportResource({ "classpath:META-INF/applicationContext-dal-dsb.xml" })
@EnableScheduling
public class DsbDalConfig {

	@Value("classpath:config/DsbMapperConfig.xml")
	Resource dsbMybatisMapperConfig;

	@Autowired
	DataSource dataSourceDsb;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}


	@Bean
	public YhzxXnzzNsrMapper yhzxXnzzNsrMapper() throws Exception {
		return newDsbMapperFactoryBean(YhzxXnzzNsrMapper.class).getObject();
	}

	@Bean
	public YhzxXnzzTpcQyMapper yhzxXnzzTpcQyMapper() throws Exception {
		return newDsbMapperFactoryBean(YhzxXnzzTpcQyMapper.class).getObject();
	}

	@Bean
	public XxzxXxmxMapper xxzxXxmxMapper() throws Exception {
		return newDsbMapperFactoryBean(XxzxXxmxMapper.class).getObject();
	}

	@Bean
	public YhzxXnzzNsrBqMapper yhzxXnzzNsrBqMapper() throws Exception {
		return newDsbMapperFactoryBean(YhzxXnzzNsrBqMapper.class).getObject();
	}



	<T> MapperFactoryBean<T> newDsbMapperFactoryBean(Class<T> clazz) throws Exception {
		final MapperFactoryBean<T> b = new MapperFactoryBean<T>();
		b.setMapperInterface(clazz);
		b.setSqlSessionFactory(dsbSqlSessionFactory());
		return b;
	}

	@Bean
	public SqlSessionFactory dsbSqlSessionFactory() throws Exception {
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(dsbMybatisMapperConfig);
		sqlSessionFactoryBean.setDataSource(dataSourceDsb);
		sqlSessionFactoryBean.setTypeAliases(new Class<?>[] {});
		return sqlSessionFactoryBean.getObject();
	}
}
