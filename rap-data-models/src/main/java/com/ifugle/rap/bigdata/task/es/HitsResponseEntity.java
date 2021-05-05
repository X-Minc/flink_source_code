package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月25日 21:17
 */
@Data
public class HitsResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("total")
    private TotalEntity total;
    @SerializedName("max_score")
    private double maxScore;
    @SerializedName("hits")
    private List<ResponseEntity<T>> hits;
}
