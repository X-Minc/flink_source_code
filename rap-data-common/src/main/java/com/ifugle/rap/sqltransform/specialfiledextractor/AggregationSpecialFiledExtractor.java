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
public class AggregationSpecialFiledExtractor implements SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> {
    private DealWithMid dealWithMid;

    public AggregationSpecialFiledExtractor(DealWithMid dealWithMid) {
        this.dealWithMid = dealWithMid;
    }

    @Override
    public List<IndexDayModel> getFormatData(JSONObject jsonObject) throws Exception {
        List<IndexDayModel> indexDayModelList = new ArrayList<>(1000000);
        JSONArray aggregations = jsonObject.getJSONObject("aggregations").getJSONObject("group_by_field").getJSONArray("buckets");
        for (Object aggregation : aggregations) {
            IndexDayModel indexDayModel = new IndexDayModel();
            JSONObject data = (JSONObject) aggregation;
            dealWithMid.dealWith(indexDayModel, data);
            indexDayModelList.add(indexDayModel);
        }
        return indexDayModelList;
    }

    public interface DealWithMid {
        void dealWith(IndexDayModel indexDayModel, JSONObject data);
    }

    public static class DealWithIncAndSuperior implements DealWithMid {

        @Override
        public void dealWith(IndexDayModel indexDayModel, JSONObject data) {
            Integer idCount = data.getJSONObject("count").getInteger("value");
            Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
            String swjgDm = data.getJSONObject("key").getString("swjg_dms");
            if (idCount != null)
                indexDayModel.setIncCount(idCount);
            indexDayModel.setOrgId(0L);
            if (swjgDm != null)
                indexDayModel.setDimData1(swjgDm);
        }
    }

    public static class DealWithIncAndSelf implements DealWithMid {

        @Override
        public void dealWith(IndexDayModel indexDayModel, JSONObject data) {
            Integer idCount = data.getJSONObject("count").getInteger("value");
            Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
            String swjgDm = data.getJSONObject("key").getString("swjg_dm");
            if (idCount != null)
                indexDayModel.setIncCount(idCount);
            if (xnzzId != null)
                indexDayModel.setOrgId((long) xnzzId);
            if (swjgDm != null)
                indexDayModel.setDimData1(swjgDm);
        }
    }

    public static class DealWithTotalAndSuperior implements DealWithMid {

        @Override
        public void dealWith(IndexDayModel indexDayModel, JSONObject data) {
            Integer idCount = data.getJSONObject("count").getInteger("value");
            Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
            String swjgDm = data.getJSONObject("key").getString("swjg_dms");
            if (idCount != null)
                indexDayModel.setTotalCount(idCount);
            indexDayModel.setOrgId(0L);
            if (swjgDm != null)
                indexDayModel.setDimData1(swjgDm);
        }
    }

    public static class DealWithTotalAndSelf implements DealWithMid {

        @Override
        public void dealWith(IndexDayModel indexDayModel, JSONObject data) {
            Integer idCount = data.getJSONObject("count").getInteger("value");
            Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
            String swjgDm = data.getJSONObject("key").getString("swjg_dm");
            if (idCount != null)
                indexDayModel.setTotalCount(idCount);
            if (xnzzId != null)
                indexDayModel.setOrgId((long) xnzzId);
            if (swjgDm != null)
                indexDayModel.setDimData1(swjgDm);
        }
    }

}
