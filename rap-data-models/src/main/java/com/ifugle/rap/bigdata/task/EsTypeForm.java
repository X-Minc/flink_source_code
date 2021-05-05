package com.ifugle.rap.bigdata.task;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年09月10日 19:44
 */
@Data
public class EsTypeForm {

    private String index;
    private String type;
    private boolean isExist;

    public EsTypeForm(String index, String type) {
        this.index = index;
        this.type = type;
    }
}
