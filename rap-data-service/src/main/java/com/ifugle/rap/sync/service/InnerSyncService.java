package com.ifugle.rap.sync.service;

import com.ifugle.rap.sqltransform.entry.BoardIndexDayModel;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;

import java.util.List;

/**
 * @author Minc
 * @date 2022/1/5 14:52
 */
public interface InnerSyncService {
    void insertIndexDay(List<IndexDayModel> indexDayModelList) throws Exception;

    void insertIndex30Day(List<IndexDayModel> indexDayModelList) throws Exception;

    void insertIndexMonth(List<IndexDayModel> indexDayModelList) throws Exception;

    void insertBoardIndexDay(List<BoardIndexDayModel> boardIndexDayModels) throws Exception;

    List<IndexDayModel> getIndexDayList() throws Exception;

    List<IndexDayModel> getIndex30DaysList() throws Exception;

    List<IndexDayModel> getIndexMonthList() throws Exception;

    List<BoardIndexDayModel> sweepUp() throws Exception;
}
