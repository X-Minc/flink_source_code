package com.ifugle.rap.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ifugle.util.NullUtil;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:13
 */
public class PageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageUtils.class);

    /**
     * 获取分页起始下标
     *
     * @param curPage
     * @param pageSize
     *
     * @return
     */
    public static int getStartNum(int curPage, int pageSize) {
        return (curPage <= 1) ? 0 : (curPage - 1) * pageSize;
    }

    /**
     * 列表分页，subList方法有弊端，新建List
     *
     * @param page
     * @param rows
     * @param voList
     * @param <T>
     *
     * @return
     */
    public static <T> List<T> paging(Integer page, Integer rows, List<T> voList) {
        if (NullUtil.isNull(page) || NullUtil.isNull(rows)) {
            LOGGER.info("无入参，数据不分页！");
            return voList;
        }
        if (page <= 0 || rows <= 0) {
            LOGGER.info("非正常入参，数据不分页！");
            return voList;
        }
        int totalCount = voList.size();
        int totalPage = (totalCount + rows - 1) / rows;
        if (page > totalPage) {
            return new ArrayList<>();
        }
        int fromIndex = rows * (page - 1);
        int toIndex = (rows * page > totalCount) ? totalCount : rows * page;
        return new ArrayList<>(voList.subList(fromIndex, toIndex));
    }

    /**
     * @param firstIndex
     * 		下标首位
     * @param pageSize
     * 		页大小
     * @param src
     * 		数据源
     * @param <T>
     * 		类型
     *
     * @return
     */
    public static <T> List<T> sub(int firstIndex, int pageSize, List<T> src) {
        if (NullUtil.isNull(src)) {
            return src;
        }
        if (pageSize <= 0) {
            return src;
        }
        int size = src.size();
        int endIndex = pageSize + firstIndex;
        if (size >= endIndex) {
            return src.subList(firstIndex, endIndex);
        } else if (size < firstIndex) {
            return new ArrayList<>();
        } else {
            return src.subList(firstIndex, size);
        }
    }

    /**
     * 获取下标首位
     *
     * @param curPage
     * @param pageSize
     *
     * @return
     */
    public static int getFirstIndex(int curPage, int pageSize) {
        return curPage <= 1 ? 0 : (curPage - 1) * pageSize;
    }

    /**
     * 获取分页总页数
     *
     * @param totalCount
     * @param pageSize
     * @return
     */
    public static int getTotalPage(int totalCount, int pageSize) {
        return (totalCount + pageSize - 1) / pageSize;
    }
}
