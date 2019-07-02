package com.ifugle.rap.canalconfig;

import javax.sql.DataSource;

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

import com.ifugle.rap.mapper.zhcs.ZxArticleMapper;

@Configuration
@ImportResource({ "classpath:META-INF/applicationContext-dal-zhcs.xml" })
@EnableScheduling
public class ZhcsDalConfig {

	@Value("classpath:config/ZhcsMapperConfig.xml")
	Resource zhcsMybatisMapperConfig;

	@Autowired
	DataSource dataSourceZhcs;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ZxArticleMapper articleMapper() throws Exception {
		return newZhcsMapperFactoryBean(ZxArticleMapper.class).getObject();
	}


	<T> MapperFactoryBean<T> newZhcsMapperFactoryBean(Class<T> clazz) throws Exception {
		final MapperFactoryBean<T> b = new MapperFactoryBean<T>();
		b.setMapperInterface(clazz);
		b.setSqlSessionFactory(zhcsSqlSessionFactory());
		return b;
	}

	@Bean
	public SqlSessionFactory zhcsSqlSessionFactory() throws Exception {
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(zhcsMybatisMapperConfig);
		sqlSessionFactoryBean.setDataSource(dataSourceZhcs);
		sqlSessionFactoryBean.setTypeAliases(new Class<?>[] {});
		return sqlSessionFactoryBean.getObject();
	}
}
