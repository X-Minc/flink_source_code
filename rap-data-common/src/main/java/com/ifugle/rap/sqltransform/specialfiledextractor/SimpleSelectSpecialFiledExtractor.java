package com.ifugle.rap.sqltransform.specialfiledextractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.sqltransform.base.SpecialFiledExtractorBase;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Minc
 * @date 2022/1/4 10:46
 */
public class SimpleSelectSpecialFiledExtractor implements SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> {

    @Override
    public List<IndexDayModel> getFormatData(JSONObject jsonObject) throws Exception {
        List<IndexDayModel> indexDayModelList = new ArrayList<>();
        JSONArray hits = jsonObject.getJSONObject("hits").getJSONArray("hits");
        JSONObject source = null;
        if (hits.size() == 1) {
            for (Object hit : hits) {
                source = ((JSONObject) hit).getJSONObject("_source");
            }
        } else {
            return indexDayModelList;
        }
        IndexDayModel indexDayModel = jsonObjToIndexDay(source);
        indexDayModelList.add(indexDayModel);
        return indexDayModelList;
    }

    private IndexDayModel jsonObjToIndexDay(JSONObject jsonObject) {
        IndexDayModel.IndexDayModelBuilder builder = IndexDayModel.builder();
        Integer cycle_id = jsonObject.getInteger("cycle_id");
        if (cycle_id != null)
            builder.setCycleId(cycle_id);
        Integer node_id = jsonObject.getInteger("node_id");
        if (node_id != null)
            builder.setNodeId(node_id);
        String index = jsonObject.getString("index");
        if (index != null)
            builder.setIndex(index);
        Integer org_id = jsonObject.getInteger("org_id");
        if (org_id != null)
            builder.setOrgId((long) org_id);
        String dim1 = jsonObject.getString("dim1");
        if (dim1 != null)
            builder.setDim1(dim1);
        String dim_data1 = jsonObject.getString("dim_data1");
        if (dim_data1 != null)
            builder.setDimData1(dim_data1);
        String dim2 = jsonObject.getString("dim2");
        if (dim2 != null)
            builder.setDim2(dim2);
        String dim_data2 = jsonObject.getString("dim_data2");
        if (dim_data2 != null)
            builder.setDimData2(dim_data2);
        String dim3 = jsonObject.getString("dim3");
        if (dim3 != null)
            builder.setDim3(dim3);
        String dim_data3 = jsonObject.getString("dim_data3");
        if (dim_data3 != null)
            builder.setDimData3(dim_data3);
        Integer total_count = jsonObject.getInteger("total_count");
        if (total_count != null)
            builder.setTotalCount(total_count);
        Integer inc_count = jsonObject.getInteger("inc_count");
        if (inc_count != null)
            builder.setIncCount(inc_count);
        return builder.build();
    }
}
