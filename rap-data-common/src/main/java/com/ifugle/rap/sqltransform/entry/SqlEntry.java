package com.ifugle.rap.sqltransform.entry;

import com.ifugle.rap.sqltransform.baseenum.KeyWord;

import java.io.Serializable;

/**
 * sql实体类，目前只支持以下关键字
 * select,from,where,group_by,order_by,limit
 *
 * @author Minc
 * @date 2021/12/31 16:37
 */
public class SqlEntry implements Serializable {
    private DataType select;
    private DataType from;
    private DataType where;
    private DataType group_by;
    private DataType order_by;
    private DataType limit;
    private DataType glimit;

    public SqlEntry() {
        select = new DataType(KeyWord.select);
        from = new DataType(KeyWord.from);
        where = new DataType(KeyWord.where);
        group_by = new DataType(KeyWord.group_by);
        order_by = new DataType(KeyWord.order_by);
        limit = new DataType(KeyWord.limit);
        glimit = new DataType(KeyWord.group_limit);
    }

    public DataType getGlimit() {
        return glimit;
    }

    public void setGlimit(DataType glimit) {
        this.glimit = glimit;
    }

    public DataType getSelect() {
        return select;
    }

    public void setSelect(DataType select) {
        this.select = select;
    }

    public DataType getFrom() {
        return from;
    }

    public void setFrom(DataType from) {
        this.from = from;
    }

    public DataType getWhere() {
        return where;
    }

    public void setWhere(DataType where) {
        this.where = where;
    }

    public DataType getGroup_by() {
        return group_by;
    }

    public void setGroup_by(DataType group_by) {
        this.group_by = group_by;
    }

    public DataType getOrder_by() {
        return order_by;
    }

    public void setOrder_by(DataType order_by) {
        this.order_by = order_by;
    }

    public DataType getLimit() {
        return limit;
    }

    public void setLimit(DataType limit) {
        this.limit = limit;
    }

    public DataType getFitSqlDataType(String data) {
        if (select.getKey().getKeyword().equals(data)) return select;
        if (from.getKey().getKeyword().equals(data)) return from;
        if (where.getKey().getKeyword().equals(data)) return where;
        if (group_by.getKey().getKeyword().equals(data)) return group_by;
        if (order_by.getKey().getKeyword().equals(data)) return order_by;
        if (limit.getKey().getKeyword().equals(data)) return limit;
        if (glimit.getKey().getKeyword().equals(data)) return glimit;
        return null;
    }
}