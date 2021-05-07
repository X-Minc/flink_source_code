package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.bigdata.task.EsTypeForm;
import com.ifugle.rap.bigdata.task.UserAllTag;
import com.ifugle.rap.bigdata.task.es.MgetResponseEntity;
import com.ifugle.rap.bigdata.task.es.ResponseEntity;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.service.EsUserAllTagService;
import com.ifugle.rap.bigdata.task.service.UserOdsService;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.constants.SjtjConfig;
import com.ifugle.rap.utils.UserOds;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:32
 */
@Service
@Slf4j
public class EsUserAllTagServiceImpl implements EsUserAllTagService {

    @Autowired
    EsService esService;

    @Autowired
    private UserOdsService userOdsService;

    @Override
    public void deleteInvalidUserAllTagByXnzzId(Long xnzzId, Date startDate, Date endDate, EsTypeForm... type) {
        log.info("执行处理用户全量标签表中的无效用户数据开始：xnzzId = {}", xnzzId);
        List<Long> tpcXnzzIds = SjtjConfig.getTpcXnzzIds();
        if (NullUtil.isNotNull(tpcXnzzIds) && tpcXnzzIds.contains(xnzzId)) {
            deleteEsAlreadyExistJgry(startDate, endDate, xnzzId, type);
            deleteEsAlreadyExistQyry(startDate, endDate, xnzzId, type);
        }
        log.info("执行处理用户全量标签表中的无效用户数据结束：xnzzId = {}", xnzzId);
    }


    @Override
    public void deleteEsAlreadyExistJgry(Date startDate, Date endDate, Long xnzzId, EsTypeForm... type) {
        checkTypeExist(type);
        Long startId = null;
        while (true) {
            List<UserOds> deleteUserList = userOdsService.listByDeleteTpcJgry(xnzzId, startId, startDate, endDate);
            if (NullUtil.isNull(deleteUserList)) {
                return;
            }
            startId = deleteUserList.get((deleteUserList.size() - 1)).getYhNsrId();
            deleteEs(deleteUserList, xnzzId);
        }
    }

    @Override
    public void deleteEsAlreadyExistQyry(Date startDate, Date endDate, Long xnzzId, EsTypeForm... type) {
        checkTypeExist(type);
        Long startId = null;
        while (true) {
            List<UserOds> deleteUserList = userOdsService.listByDeleteTpcQyry(xnzzId, startId, startDate, endDate);
            if (NullUtil.isNull(deleteUserList)) {
                return;
            }
            startId = deleteUserList.get((deleteUserList.size() - 1)).getYhNsrId();
            deleteEs(deleteUserList, xnzzId);
        }
    }

    @Override
    public List<UserAllTag> listByKeyIds(List<String> keyIds) {
        List<ResponseEntity<UserAllTag>> result = esService.multiGet(EsIndexConstant.USER_ALL_TAG,
                keyIds, new TypeToken<MgetResponseEntity<UserAllTag>>() {
                }.getType());
        if (NullUtil.isNull(result)) {
            return new ArrayList<>();
        }

        List<UserAllTag> userAllTagList = result.stream().map(entity -> entity.getSource()).collect(Collectors.toList());
        return userAllTagList;
    }

    /**
     * 判断es type是否存在
     *
     * @param type
     */
    private void checkTypeExist(EsTypeForm[] type) {
        for (EsTypeForm esTypeForm : type) {
            boolean typeExist = esService.getIndexExist(esTypeForm.getIndex());
            esTypeForm.setExist(typeExist);
        }
    }


    /**
     * 删除es中的数据
     *
     * @param type
     * @param deleteUserList
     * @param xnzzId
     */
    private void deleteEs(List<UserOds> deleteUserList, Long xnzzId, EsTypeForm... type) {
        List<String> keyIds = Lists.newArrayList();
        for (UserOds user : deleteUserList) {
            String userOdsKey = EsKeyUtil.getUserOdsKey(user);
            keyIds.add(userOdsKey);
        }

        if (keyIds.isEmpty()) {
            log.debug("删除es中的用户key为空：xnzzId:{}", xnzzId);
            return;
        }

        for (EsTypeForm esTypeForm : type) {
            deleteEs(keyIds, esTypeForm);
        }
    }

    /**
     * 删除es中的数据
     *
     * @param keyIds
     * @param esTypeForm
     */
    private void deleteEs(List<String> keyIds, EsTypeForm esTypeForm) {
        boolean exist = esTypeForm.isExist();
        if (exist) {
            esService.multiDelete(esTypeForm.getIndex(), keyIds);
        }
    }
}
