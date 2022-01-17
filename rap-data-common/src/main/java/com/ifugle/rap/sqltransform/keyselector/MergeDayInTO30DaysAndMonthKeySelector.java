package com.ifugle.rap.sqltransform.keyselector;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * @author Minc
 * @date 2022/1/17 11:32
 */
public class MergeDayInTO30DaysAndMonthKeySelector implements KeySelector<IndexDayModel> {
    @Override
    public String getKey(IndexDayModel indexDayModel) {
        return
//                indexDayModel.getCycleId() + "," +
//                indexDayModel.getNodeId() + "," +
                indexDayModel.getIndex() + "," +
                indexDayModel.getOrgId() + "," +
                indexDayModel.getDim1() + "," +
                indexDayModel.getDimData1() + "," +
                indexDayModel.getDim2() + "," +
                indexDayModel.getDimData2() + "," +
                indexDayModel.getDim3() + "," +
                indexDayModel.getDimData3();
    }
}
