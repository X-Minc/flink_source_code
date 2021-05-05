package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月25日 11:19
 */
@Data
public class MgetResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("docs")
    private List<ResponseEntity<T>> docs;
}
