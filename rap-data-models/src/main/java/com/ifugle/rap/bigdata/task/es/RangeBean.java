package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;

import lombok.Data;

/**
 * es range 范围查询条件类
 *
 * @author GuanTao
 * @version $Id$
 * @since 2019年09月01日 12:33
 */
@Data
public class RangeBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String gte;
    private String gt;
    private String lte;
    private String lt;
    private String format;
}
