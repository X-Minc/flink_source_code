package com.ifugle.rap.sqltransform.samekeyaction;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SameKeyAction;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * @author Minc
 * @date 2022/1/7 11:11
 */
public class DayMergeBeforeWorkerSame extends SameKeyAction<IndexDayModel> {

    public DayMergeBeforeWorkerSame(KeySelector<IndexDayModel> indexDayModelKeySelector) {
        super(indexDayModelKeySelector);
    }

    @Override
    public void sameKeyDone(IndexDayModel remain, IndexDayModel leave) {
        int num = remain.getTotalCount() - leave.getTotalCount();
        if (num < 0)
            remain.setDecCount(Math.abs(num));
        else {
            remain.setIncCount(Math.abs(num));
            remain.setNetIncCount(num);
        }
    }
}
