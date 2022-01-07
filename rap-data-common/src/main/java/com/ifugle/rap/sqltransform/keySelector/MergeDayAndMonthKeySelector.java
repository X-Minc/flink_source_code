package com.ifugle.rap.sqltransform.keySelector;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

import java.security.Key;

/**
 * @author Minc
 * @date 2022/1/7 10:33
 */
public class MergeDayAndMonthKeySelector implements KeySelector<IndexDayModel> {
    @Override
    public String getKey(IndexDayModel indexDayModel) {
        return indexDayModel.getCycleId() + "," +
                indexDayModel.getNodeId() + "," +
                indexDayModel.getIndex() + "," +
                indexDayModel.getOrgId() + "," +
                indexDayModel.getDim1() + "," +
                indexDayModel.getDimData1() + "," +
                indexDayModel.getDim2() + "," +
                indexDayModel.getDimData2() + "," +
                indexDayModel.getDim3() + "," +
                indexDayModel.getDimData3();
    }

    @Override
    public void sameKeyDone(IndexDayModel remain, IndexDayModel leave) {
        remain.setTotalCount(leave.getTotalCount());
    }
}
