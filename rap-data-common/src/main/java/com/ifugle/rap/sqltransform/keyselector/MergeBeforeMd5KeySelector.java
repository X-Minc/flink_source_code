package com.ifugle.rap.sqltransform.keyselector;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.utils.MD5Util;

/**
 * @author Minc
 * @date 2022/1/17 11:40
 */
public class MergeBeforeMd5KeySelector implements KeySelector<IndexDayModel> {
    @Override
    public String getKey(IndexDayModel indexDayModel) {
        return MD5Util.stringToMD5(indexDayModel.getIndex() + "," +
                indexDayModel.getOrgId() + "," +
                indexDayModel.getDim1() + "," +
                indexDayModel.getDimData1() + "," +
                indexDayModel.getDim2() + "," +
                indexDayModel.getDimData2() + "," +
                indexDayModel.getDim3() + "," +
                indexDayModel.getDimData3());
    }
}
