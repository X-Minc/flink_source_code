/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.es;

import lombok.Data;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月31日 11:28
 */
@Data
public class DeleteRangeRequest extends RangeRequestBuild {

    public DeleteRangeRequest() {

    }

    public DeleteRangeRequest(String param, String gte, String lte, String format) {
        super();
        setRangeParam(param, gte, lte, format);
    }
}
