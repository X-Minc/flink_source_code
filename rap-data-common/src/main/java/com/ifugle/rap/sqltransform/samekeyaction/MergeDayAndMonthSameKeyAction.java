package com.ifugle.rap.sqltransform.samekeyaction;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SameKeyAction;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * @author Minc
 * @date 2022/1/7 10:33
 */
public class MergeDayAndMonthSameKeyAction extends SameKeyAction<IndexDayModel> {


    public MergeDayAndMonthSameKeyAction(KeySelector<IndexDayModel> indexDayModelKeySelector) {
        super(indexDayModelKeySelector);
    }

    @Override
    public void sameKeyDone(IndexDayModel remain, IndexDayModel leave) {
        remain.setTotalCount(leave.getTotalCount());
    }
}
