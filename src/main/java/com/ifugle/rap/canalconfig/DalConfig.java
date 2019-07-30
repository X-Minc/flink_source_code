package com.ifugle.rap.canalconfig;

import javax.sql.DataSource;

import com.ifugle.rap.mapper.BizDataMapper;
import com.ifugle.rap.mapper.BotChatResponseMessageDOMapper;
import com.ifugle.rap.mapper.BotConfigServerMapper;
import com.ifugle.rap.mapper.BotMediaDOMapper;
import com.ifugle.rap.mapper.BotOutoundTaskDetailMapper;
import com.ifugle.rap.mapper.BotTrackDetailDOMapper;
import com.ifugle.rap.mapper.BotUnawareDetailDOMapper;
import com.ifugle.rap.mapper.KbsArticleDOMapper;
import com.ifugle.rap.mapper.KbsKeywordDOMapper;
import com.ifugle.rap.mapper.KbsQuestionArticleDOMapper;
import com.ifugle.rap.mapper.KbsQuestionDOMapper;
import com.ifugle.rap.mapper.KbsReadingDOMapper;
import com.ifugle.rap.mapper.KbsTagDTOMapper;
import com.ifugle.rap.mapper.YhzxxnzzcyDOMapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ImportResource({ "classpath:META-INF/applicationContext-dal.xml" })
@EnableScheduling
public class DalConfig {

    @Value("classpath:config/MapperConfig.xml")
    Resource mybatisMapperConfig;

    @Autowired
    DataSource dataSource;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public BotChatResponseMessageDOMapper BotChatResponseMessageDOMapper() throws Exception {
        return newMapperFactoryBean(BotChatResponseMessageDOMapper.class).getObject();
    }

    @Bean
    public BotTrackDetailDOMapper BotTrackDetailDOMapper() throws Exception {
        return newMapperFactoryBean(BotTrackDetailDOMapper.class).getObject();
    }

    @Bean
    public BotUnawareDetailDOMapper BotUnawareDetailDOMapper() throws Exception {
        return newMapperFactoryBean(BotUnawareDetailDOMapper.class).getObject();
    }

    @Bean
    public KbsArticleDOMapper KbsArticleDOMapper() throws Exception {
        return newMapperFactoryBean(KbsArticleDOMapper.class).getObject();
    }

    @Bean
    public KbsQuestionArticleDOMapper KbsQuestionArticleDOMapper() throws Exception {
        return newMapperFactoryBean(KbsQuestionArticleDOMapper.class).getObject();
    }

    @Bean
    public KbsQuestionDOMapper KbsQuestionDOMapper() throws Exception {
        return newMapperFactoryBean(KbsQuestionDOMapper.class).getObject();
    }

    @Bean
    public KbsReadingDOMapper KbsReadingDOMapper() throws Exception {
        return newMapperFactoryBean(KbsReadingDOMapper.class).getObject();
    }

    @Bean
    public KbsKeywordDOMapper KbsKeywordDOMapper() throws Exception {
        return newMapperFactoryBean(KbsKeywordDOMapper.class).getObject();
    }

    @Bean
    public YhzxxnzzcyDOMapper YhzxxnzzcyDOMapper() throws Exception {
        return newMapperFactoryBean(YhzxxnzzcyDOMapper.class).getObject();
    }

    @Bean
    public BotMediaDOMapper BotMediaDOMapper() throws Exception {
        return newMapperFactoryBean(BotMediaDOMapper.class).getObject();
    }

    @Bean
    public KbsTagDTOMapper KbsTagDTOMapper() throws Exception {
        return newMapperFactoryBean(KbsTagDTOMapper.class).getObject();
    }

    @Bean
    public BizDataMapper bizDataMapper() throws Exception {
        return newMapperFactoryBean(BizDataMapper.class).getObject();
    }

    @Bean
    public BotConfigServerMapper botConfigServerMapper() throws Exception {
        return newMapperFactoryBean(BotConfigServerMapper.class).getObject();
    }

    @Bean
    public BotOutoundTaskDetailMapper botOutoundTaskDetailMapper() throws Exception {
        return newMapperFactoryBean(BotOutoundTaskDetailMapper.class).getObject();
    }

    <T> MapperFactoryBean<T> newMapperFactoryBean(Class<T> clazz) throws Exception {
        final MapperFactoryBean<T> b = new MapperFactoryBean<T>();
        b.setMapperInterface(clazz);
        b.setSqlSessionFactory(sqlSessionFactory());
        return b;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setConfigLocation(mybatisMapperConfig);
        fb.setDataSource(dataSource);
        fb.setTypeAliases(new Class<?>[] {});
        return fb.getObject();
    }
}
