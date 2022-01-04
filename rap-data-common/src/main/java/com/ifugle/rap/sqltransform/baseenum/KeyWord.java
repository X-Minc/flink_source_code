package com.ifugle.rap.sqltransform.baseenum;

/**
 * 关键字枚举类
 *
 * @author Minc
 * @date 2021/12/31 16:14
 */
public enum KeyWord {
    select("select"),
    from("from"),
    where("where"),
    group_by("group by"),
    order_by("order by"),
    limit("limit"),
    group_limit("glimit");

    private final String keyword;

    KeyWord(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

}
