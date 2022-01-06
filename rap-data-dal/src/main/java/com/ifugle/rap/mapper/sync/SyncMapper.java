package com.ifugle.rap.mapper.sync;

import com.ifugle.rap.sqltransform.entry.IndexDayModel;

import java.util.List;

/**
 * @author Minc
 * @date 2022/1/5 11:30
 */
public interface SyncMapper {
    void insertIndexDay(List<IndexDayModel> indexDayModelList);
    void insertIndex30Day(List<IndexDayModel> indexDayModelList);
    void insertIndexMonth(List<IndexDayModel> indexDayModelList);
}
