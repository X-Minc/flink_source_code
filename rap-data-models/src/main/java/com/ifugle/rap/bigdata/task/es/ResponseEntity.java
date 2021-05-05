package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月25日 11:23
 */
@Data
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("_index")
    private String index;

    @SerializedName("_type")
    private String type;

    /**
     * es keyId
     */
    @SerializedName("_id")
    private String id;

    @SerializedName("_version")
    private int version;

    @SerializedName("_score")
    private double score;

    /**
     * 实际内容
     */
    @SerializedName("_source")
    private T source;

    @SerializedName("found")
    private boolean found;

    @SerializedName("_parent")
    private String parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseEntity<?> entity = (ResponseEntity<?>) o;
        return version == entity.version &&
                Double.compare(entity.score, score) == 0 &&
                found == entity.found &&
                Objects.equals(index, entity.index) &&
                Objects.equals(type, entity.type) &&
                Objects.equals(id, entity.id) &&
                Objects.equals(source, entity.source) &&
                Objects.equals(parent, entity.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, type, id, version, score, source, found, parent);
    }
}
