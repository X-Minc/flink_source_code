package com.ifugle.rap.bigdata.task;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2020年02月04日 9:50
 */
@Data
public class BiDmSwjgVO extends BiDmSwjg {
    private static final long serialVersionUID = 1L;

    /**
     * 是否有下级税务机关
     */
    private Boolean hasSub;
}
