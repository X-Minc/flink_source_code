package com.ifugle.rap.bigdata.task.es;

import java.util.List;

import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;

/**
 * @author JiangYangfan
 * @version $Id: SpecificRealtimeSearchResponse.java 98473 2019-05-09 13:29:05Z JiangYangfan $
 * @since 2019年05月09日 16:56
 */
public class SpecificRealtimeSearchResponse<T> extends RealtimeSearchResponse {
    private static final long serialVersionUID = 1L;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
