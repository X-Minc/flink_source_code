package com.ifugle.rap.sqltransform.keySelector;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.utils.MD5Util;

/**
 * @author Minc
 * @date 2022/1/7 11:11
 */
public class Days30MergeBeforeSelector implements KeySelector<IndexDayModel> {
    @Override
    public String getKey(IndexDayModel indexDayModel) {
        return getMergeBeforeKey(indexDayModel);
    }

    private String getMergeBeforeKey(IndexDayModel indexDayModel) {
        return MD5Util.stringToMD5(indexDayModel.getIndex() + "," +
                indexDayModel.getOrgId() + "," +
                indexDayModel.getDim1() + "," +
                indexDayModel.getDimData1() + "," +
                indexDayModel.getDim2() + "," +
                indexDayModel.getDimData2() + "," +
                indexDayModel.getDim3() + "," +
                indexDayModel.getDimData3());
    }

    @Override
    public void sameKeyDone(IndexDayModel remain, IndexDayModel leave) {
        int num = remain.getIncCount() - leave.getIncCount();
        if (num < 0)
            remain.setDecCount(Math.abs(num));
        else {
            remain.setNetIncCount(num);
        }
    }
}
