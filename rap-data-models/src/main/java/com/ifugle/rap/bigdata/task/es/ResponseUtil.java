package com.ifugle.rap.bigdata.task.es;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;

/**
 * @author JiangYangfan
 * @version $Id: ResponseUtil.java 98473 2019-05-09 13:29:05Z JiangYangfan $
 * @since 2019年05月09日 18:03
 */
public class ResponseUtil {
    private static final Gson gson = new GsonBuilder().create();

    public static <T> SpecificRealtimeSearchResponse build(RealtimeSearchResponse rep, Class<T> cls) {
        SpecificRealtimeSearchResponse<T> tRep = new SpecificRealtimeSearchResponse<>();
        BeanUtils.copyProperties(rep, tRep);
        tRep.setData(getDataList(gson.toJson(rep.getHits()), cls));
        return tRep;
    }

    private static <T> List<T> getDataList(String strJson, Class<T> cls) {
        List<T> datalist = new ArrayList<>();
        if (null == strJson) {
            return datalist;
        }

        Type type = new ListParameterizedType(cls);
        datalist = gson.fromJson(strJson, type);

        return datalist;
    }

    private static class ListParameterizedType implements ParameterizedType {
        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] { type };
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        // implement equals method too! (as per javadoc)
    }
}
