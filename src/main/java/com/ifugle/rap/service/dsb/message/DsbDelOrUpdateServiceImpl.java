/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.dsb.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.service.BusinessCommonService;

/**
 * @author HuangLei(wenyuan)
 * @version $Id DsbDelOrUpdateService.java v 0.1 2019/7/29 HuangLei(wenyuan) Exp $
 */
@Service
public class DsbDelOrUpdateServiceImpl implements DsbDelOrUpdateService {

    @Autowired
    private BusinessCommonService businessCommonService;

    public boolean deleteYhzxXnzzNsr(String id,String tableName) {
        return businessCommonService.delete(ChannelType.YHZX_XNZZ_NSR.getCode(), tableName, id);
    }

}
