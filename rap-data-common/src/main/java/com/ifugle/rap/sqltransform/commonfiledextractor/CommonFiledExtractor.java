package com.ifugle.rap.sqltransform.commonfiledextractor;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.utils.TimeUtil;

import java.util.Calendar;
import java.util.List;

/**
 * @author Minc
 * @date 2022/1/5 15:11
 */
public class CommonFiledExtractor implements CommonFiledExtractorBase<JSONObject, List<IndexDayModel>>, Cloneable {
    private String index = "";
    private String dim1 = "";
    private String dim2 = "";
    private String dimData2 = "";
    private String dim3 = "";
    private String dimData3 = "";
    private Boolean isMonthTask = false;


    public static CommonFiledExtractorBuilder builder() {
        return new CommonFiledExtractorBuilder(new CommonFiledExtractor());
    }

    @Override
    public List<IndexDayModel> customSetData(List<IndexDayModel> indexDayModelList) throws Exception {
        String cycleIdAndNodeId = isMonthTask ? TimeUtil.getBeforeTime(Calendar.MONTH, -1, "yyyyMM") :
                TimeUtil.getStringDate(System.currentTimeMillis(), "yyyyMMdd", -1000L * 60 * 60 * 24);
        for (IndexDayModel indexDayModel : indexDayModelList) {
            indexDayModel.setCycleId(Integer.parseInt(cycleIdAndNodeId));
            indexDayModel.setNodeId(Integer.parseInt(cycleIdAndNodeId));
            indexDayModel.setIndex(index);
            indexDayModel.setDim1(dim1);
            indexDayModel.setDim2(dim2);
            indexDayModel.setDimData2(dimData2);
            indexDayModel.setDim3(dim3);
            indexDayModel.setDimData3(dimData3);
        }
        return indexDayModelList;
    }

    @Override
    public CommonFiledExtractor clone() {
        try {
            CommonFiledExtractor clone = (CommonFiledExtractor) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**************************************************************************************************************************
     * builder
     *************************************************************************************************************************/
    public static class CommonFiledExtractorBuilder {

        private final CommonFiledExtractor commonFiledExtractor;

        public CommonFiledExtractorBuilder(CommonFiledExtractor commonFiledExtractor) {
            this.commonFiledExtractor = commonFiledExtractor;
        }

        public CommonFiledExtractorBuilder setIndex(String index) {
            this.commonFiledExtractor.index = index;
            return this;
        }

        public CommonFiledExtractorBuilder setDim1(String dim1) {
            this.commonFiledExtractor.dim1 = dim1;
            return this;
        }

        public CommonFiledExtractorBuilder setDim2(String dim2) {
            this.commonFiledExtractor.dim2 = dim2;
            return this;
        }

        public CommonFiledExtractorBuilder setDimData2(String dimData2) {
            this.commonFiledExtractor.dimData2 = dimData2;
            return this;
        }

        public CommonFiledExtractorBuilder setDim3(String dim3) {
            this.commonFiledExtractor.dim3 = dim3;
            return this;
        }

        public CommonFiledExtractorBuilder setDimData3(String dimData3) {
            this.commonFiledExtractor.dimData3 = dimData3;
            return this;
        }

        public CommonFiledExtractorBuilder setMonthTask(Boolean monthTask) {
            this.commonFiledExtractor.isMonthTask = monthTask;
            return this;
        }

        public CommonFiledExtractor build() throws CloneNotSupportedException {
            return commonFiledExtractor.clone();
        }
    }
}
