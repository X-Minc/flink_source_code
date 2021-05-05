package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年09月12日 19:13
 */
@Data
public class BulkResponseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("errors")
    private boolean errors;
    @SerializedName("took")
    private int took;
    @SerializedName("items")
    private List<BulkEntity> items;
}
