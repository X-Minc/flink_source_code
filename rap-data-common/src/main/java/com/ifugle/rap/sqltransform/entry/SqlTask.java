package com.ifugle.rap.sqltransform.entry;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.SpecialFiledExtractorBase;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.specialfiledextractor.SingleAggregationSpecialFiledExtractor;

import java.util.List;

/**
 * @author Minc
 * @date 2022/1/6 10:16
 */
public class SqlTask {
    private boolean isLevel;
    private String sql;
    /**
     * 1. bi_index_day
     * 2. bi_index_30day
     * 3. bi_index_month
     */
    private Integer tableType;
    private SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> specialFiledExtractorBase;
    private CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> commonFiledExtractor;

    public SqlTask(String sql, Integer tableType, SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> specialFiledExtractorBase,  CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> commonFiledExtractor,boolean isLevel) {
        this.sql = sql;
        this.tableType = tableType;
        this.specialFiledExtractorBase = specialFiledExtractorBase;
        this.commonFiledExtractor = commonFiledExtractor;
        this.isLevel=isLevel;
    }

    public boolean isLevel() {
        return isLevel;
    }

    public void setLevel(boolean level) {
        isLevel = level;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> getSpecialFiledExtractorBase() {
        return specialFiledExtractorBase;
    }

    public void setSpecialFiledExtractorBase(SingleAggregationSpecialFiledExtractor specialFiledExtractorBase) {
        this.specialFiledExtractorBase = specialFiledExtractorBase;
    }

    public CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> getCommonFiledExtractor() {
        return commonFiledExtractor;
    }

    public void setCommonFiledExtractor(CommonFiledExtractor commonFiledExtractor) {
        this.commonFiledExtractor = commonFiledExtractor;
    }
}
