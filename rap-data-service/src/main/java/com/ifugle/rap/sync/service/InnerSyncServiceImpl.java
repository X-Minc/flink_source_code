package com.ifugle.rap.sync.service;

import com.ifugle.rap.mapper.sync.SyncMapper;
import com.ifugle.rap.sqltransform.entry.BoardIndexDayModel;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Minc
 * @date 2022/1/5 14:53
 */
@Service
public class InnerSyncServiceImpl implements InnerSyncService {

    @Autowired
    SyncMapper syncMapper;

    @Override
    public void insertIndexDay(List<IndexDayModel> indexDayModelList) throws Exception {
        syncMapper.insertIndexDay(indexDayModelList);
    }

    @Override
    public void insertIndex30Day(List<IndexDayModel> indexDayModelList) throws Exception {
        syncMapper.insertIndex30Day(indexDayModelList);
    }

    @Override
    public void insertIndexMonth(List<IndexDayModel> indexDayModelList) throws Exception {
        syncMapper.insertIndexMonth(indexDayModelList);
    }

    @Override
    public void insertBoardIndexDay(List<BoardIndexDayModel> boardIndexDayModels) throws Exception {
        syncMapper.insertBoardIndexDay(boardIndexDayModels);
    }

    @Override
    public List<IndexDayModel> getIndexDayList() throws Exception {
        return syncMapper.getIndexDayList();
    }

    @Override
    public List<IndexDayModel> getIndex30DaysList() throws Exception {
        return syncMapper.getIndex30DaysList();
    }

    @Override
    public List<IndexDayModel> getIndexMonthList() throws Exception {
        return syncMapper.getIndexMonthList();
    }

    @Override
    public List<BoardIndexDayModel> sweepUp() throws Exception {
        return syncMapper.sweepUp();
    }
}
