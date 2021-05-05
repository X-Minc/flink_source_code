/**
 * Copyright(C) 2019 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper.bigdata;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.BiDmSwjgPathVo;
import com.ifugle.rap.bigdata.task.BiDmSwjgVO;
import com.ifugle.rap.core.mapper.BaseMapper;

/**
 * @author Administrator
 * @version $Id$
 * @since 2019-08-12 13:48:29
 */
public interface BiDmSwjgMapper extends BaseMapper<BiDmSwjg, String> {
    /**
     * 查询swjgdm以某个值开头的
     *
     * @param swjgdmPre
     *
     * @return
     */
    List<BiDmSwjg> getSwjgBySwjgdmPre(@Param("swjgdmPre") String swjgdmPre);

    /**
     * 查询下级税务机关
     *
     * @param sjswjgDm
     *
     * @return
     */
    List<BiDmSwjg> getSubSwjgBySwjgdm(@Param("sjswjgDm") String sjswjgDm);

    /**
     * 根据税务机关代码层级全路径查询匹配的所有税务机关
     *
     * @param sjswjgPath
     *
     * @return
     */
    List<BiDmSwjg> getSwjgBySwjgdmPathPre(@Param("sjswjgPath") String sjswjgPath);

    /**
     * 查询所有有效的税务机关
     *
     * @return
     */
    List<String> listSwjgDmAll();

    /**
     * 查询所有使用标志=1的税务机关
     *
     * @return
     */
    List<String> listSwjgDmBySybz();



    String getSwjgDmByXnzzId(@Param("xnzzId") Long xnzzId);

    /**
     * 根据swjgDms，获取税务机关代码列表
     *
     * @param swjgDms
     *
     * @return
     */
    List<BiDmSwjg> listBiDmSwjgs(@Param("swjgDms") List<String> swjgDms);

    /**
     * 根据税务机关代码获取相关数据及是否有下级税务机关
     *
     * @param swjgdm
     *
     * @return
     */
    Map<String, Object> selectSwjgAndSubById(@Param("swjgDm") String swjgdm);

    /**
     * 根据虚拟组织ID查询税务机关信息
     *
     * @param xnzzId
     * @return
     */
    BiDmSwjg getSwjgByXnzzId(@Param("xnzzId") Long xnzzId);

    /**
     * 根据swjgDms，获取下级税务机关代码列表
     *
     * @param swjgDms
     *
     * @return
     */
    List<BiDmSwjg> listSubBiDmSwjgs(@Param("swjgDms") List<String> swjgDms);



    /**
     * 获取下级税务机关
     *
     * @param swjgDm
     * @return
     */
    List<BiDmSwjgVO> listSubSwjg(@Param("swjgDm") String swjgDm);

    /**
     * 按名称模糊查询税务机关
     *
     * @param swjgmc
     * @return
     */
    List<BiDmSwjg> listSwjgBySwjgmc(@Param("swjgmc") String swjgmc);

    /**
     * 按税务机关代码模糊匹配
     *
     * @param sjswjgDm
     * @param swjgDm
     * @return
     */
    List<BiDmSwjgVO> listBiDmSwjgsBySjswjgDmAndLikeSwjgDm(@Param("sjswjgDm") String sjswjgDm,
            @Param("swjgDm") String swjgDm);

    /**
     * 按税务机关代码获取信息，包含所属省市区名称
     *
     * @param swjgDms
     * @return
     */
    List<BiDmSwjgPathVo> listSwjgIncludePath(@Param("swjgDms") List<String> swjgDms);

    /**
     * 查询指定税务机关代码和其下级所有税务机构代码
     * @param swjgDm
     * @return
     */
    List<String> listSwjgDmIncludeSub(@Param("swjgDm") String swjgDm);



    /**
     * 查询需要全量抽取的虚拟组织
     *
     * @param xnzzId
     *
     * @return
     */
    List<BiDmSwjg> listXnzzForAllInsert(@Param("xnzzId") Long xnzzId);

    /**
     * 查询需要增量量抽取的虚拟组织
     *
     * @param xnzzId
     *
     * @return
     */
    List<BiDmSwjg> listXnzzForUpdate(@Param("xnzzId") Long xnzzId);

    /**
     * 查询部门列表
     *
     * @param xnzzId
     *
     * @return
     */
    List<BiDmSwjg> listBmByXnzzId(@Param("xnzzId") Long xnzzId);

    List<BiDmSwjg> listBmByXnzzIds(@Param("xnzzIds") List<Long> xnzzIds);

    /**
     * 查询所有虚拟组织列表
     *
     * @param qyrq
     *
     * @return
     */
    List<BiDmSwjg> listAllXnzz(@Param("qyrq") Date qyrq);

    /**
     * 获取全部有效税务机关
     *
     * @return
     */
    List<BiDmSwjg> listAll();

    /**
     * 根据qyrq查询，当天增加的组织
     *
     * @param qyrq
     * @param xnzzId
     *
     * @return
     */
    List<BiDmSwjg> listXnzzByQyrq(@Param("xnzzId") Long xnzzId, @Param("qyrq") String qyrq);

    /**
     * 查询浙江税务机关虚拟组织
     *
     * @return
     */
    List<BiDmSwjg> listXnzzForZJSW();

    Integer getMaxSwjgcj();

    /**
     * 根据税务机关代码查询虚拟组织数量
     *
     * @param swjgDm
     * @return
     */
    Integer getXnzzCountBySwjg(@Param("swjgDm") String swjgDm);


    /**
     * 获取全部有效税务机关
     *
     * @return
     */
    List<BiDmSwjg> listAllQybz();
}