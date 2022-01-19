package com.ifugle.rap.sqltransform.samekeyaction;

import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SameKeyAction;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

/**
 * @author Minc
 * @date 2022/1/7 10:33
 */
public class MergeTotalCountInLastMonthAndYesterdaySameKeyAction extends SameKeyAction<IndexDayModel> {


    public MergeTotalCountInLastMonthAndYesterdaySameKeyAction(KeySelector<IndexDayModel> indexDayModelKeySelector) {
        super(indexDayModelKeySelector);
    }

    @Override
    public void sameKeyAction(IndexDayModel remain, IndexDayModel leave) throws Exception {
        remain.setTotalCount(leave.getTotalCount());
    }
}
