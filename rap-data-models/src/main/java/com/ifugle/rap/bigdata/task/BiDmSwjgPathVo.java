package com.ifugle.rap.bigdata.task;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2020年05月29日 13:52
 */
@Data
public class BiDmSwjgPathVo extends BiDmSwjg {
    private static final long serialVersionUID = 1L;

    /**
     * 省份
     */
    private String province;

    /**
     * 地级市
     */
    private String city;

    /**
     * 区县
     */
    private String district;
}
