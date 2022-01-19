package com.ifugle.rap.sqltransform.samekeyaction;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SameKeyAction;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * @author Minc
 * @date 2022/1/7 11:11
 */
public class MergeLastMonthAndLastTwoMonthInMonthlyTaskSameKeyAction extends SameKeyAction<IndexDayModel> {


    public MergeLastMonthAndLastTwoMonthInMonthlyTaskSameKeyAction(KeySelector<IndexDayModel> indexDayModelKeySelector) {
        super(indexDayModelKeySelector);
    }

    @Override
    public void sameKeyAction(IndexDayModel remain, IndexDayModel leave) throws Exception {
        int num = remain.getTotalCount() - leave.getTotalCount();
        if (num < 0)
            remain.setDecCount(Math.abs(num));
        else {
            remain.setNetIncCount(num);
        }
    }
}
