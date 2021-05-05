package com.ifugle.rap.bigdata.task.es;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2020年01月02日 15:39
 */
@Data
public class SetResultResponseEntity {

    @SerializedName("acknowledged")
    private Boolean acknowledged;
}
