package com.ifugle.rap.sqltransform.commonfiledextractor;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

import java.util.List;

/**
 * @author Minc
 * @date 2022/1/5 15:11
 */
public class CommonFiledExtractor implements CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> {

    private final Integer cycleId;
    private final Integer nodeId;
    private final String index;
    private final String dim1;
    private final String dim2;
    private final String dimData2;
    private final String dim3;
    private final String dimData3;

    public CommonFiledExtractor(Integer cycleId, Integer nodeId, String index, String dim1, String dim2, String dimData2, String dim3, String dimData3) {
        this.cycleId = cycleId;
        this.nodeId = nodeId;
        this.index = index;
        this.dim1 = dim1;
        this.dim2 = dim2;
        this.dimData2 = dimData2;
        this.dim3 = dim3;
        this.dimData3 = dimData3;
    }

    @Override
    public List<IndexDayModel> customSetData(List<IndexDayModel> indexDayModelList) throws Exception {
        for (IndexDayModel indexDayModel : indexDayModelList) {
            indexDayModel.setCycleId(cycleId);
            indexDayModel.setNodeId(nodeId);
            indexDayModel.setIndex(index);
            indexDayModel.setDim1(dim1);
            indexDayModel.setDim2(dim2);
            indexDayModel.setDimData2(dimData2);
            indexDayModel.setDim3(dim3);
            indexDayModel.setDimData3(dimData3);
        }
        return indexDayModelList;
    }
}
