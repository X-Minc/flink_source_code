package com.ifugle.rap.bigdata.task.service;

import java.util.List;

import com.ifugle.rap.bigdata.task.BiDmSwjg;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:02
 */
public interface BiDmSwjgService {

    /**
     * 查询需要全量抽取的虚拟组织
     *
     * @return
     */
    List<BiDmSwjg> listXnzzForAllInsert();

    /**
     * 按虚拟组织ID查询需要增量量抽取的虚拟组织
     *
     * @param xnzzId
     *
     * @return
     */
    BiDmSwjg getXnzzForUpdate(Long xnzzId);

    /**
     * 查询需要增量量抽取的虚拟组织
     *
     * @return
     */
    List<BiDmSwjg> listXnzzForUpdate();
}
