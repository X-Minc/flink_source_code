package com.ifugle.rap.canalconfig;

import javax.sql.DataSource;

import com.ifugle.rap.mapper.sca.BotScaTaskResultDOMapper;
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
@ImportResource({"classpath:META-INF/applicationContext-dal-sca.xml"})
@EnableScheduling
public class ScaDalConfig {

	@Value("classpath:config/ScaMapperConfig.xml")
	Resource scaMybatisMapperConfig;

	@Autowired
	DataSource dataSourceSca;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ZxArticleMapper articleMapper() throws Exception {
		return newScaMapperFactoryBean(ZxArticleMapper.class).getObject();
	}

	@Bean
	public BotScaTaskResultDOMapper botScaTaskResultDOMapper() throws Exception {
		return newScaMapperFactoryBean(BotScaTaskResultDOMapper.class).getObject();
	}


	<T> MapperFactoryBean<T> newScaMapperFactoryBean(Class<T> clazz) throws Exception {
		final MapperFactoryBean<T> b = new MapperFactoryBean<T>();
		b.setMapperInterface(clazz);
		b.setSqlSessionFactory(scaSqlSessionFactory());
		return b;
	}

	@Bean
	public SqlSessionFactory scaSqlSessionFactory() throws Exception {
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(scaMybatisMapperConfig);
		sqlSessionFactoryBean.setDataSource(dataSourceSca);
		sqlSessionFactoryBean.setTypeAliases(new Class<?>[] {});
		return sqlSessionFactoryBean.getObject();
	}
}
