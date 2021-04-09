/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.dsb.message;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id DsbDelOrUpdateService.java v 0.1 2019/7/29 HuangLei(wenyuan) Exp $
 */
public interface DsbDelOrUpdateService {

    /***
     * 处理丁税宝用户中心虚拟组织纳税人删除操作
     * @return
     */
    boolean deleteYhzxXnzzNsr(String id, String tableName);

}
