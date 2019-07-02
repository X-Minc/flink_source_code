/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ifugle.rap.mapper.zhcs.ZxArticleMapper;
import com.ifugle.rap.model.zhcs.ZxArticle;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id ZhcsMybatisTest.java v 0.1 2019/1/4 HuangLei(wenyuan) Exp $
 */
public class ZhcsMybatisTest extends ZhcsBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    private ZxArticleMapper zxArticleMapper;

    @Test
    public void testArticle(){
        ZxArticle article =  zxArticleMapper.selectByPrimaryKey(200001L);
        logger.info(JSON.toJSONString(article));
    }
}
