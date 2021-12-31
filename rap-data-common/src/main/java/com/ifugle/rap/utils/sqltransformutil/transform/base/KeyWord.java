package com.ifugle.rap.utils.sqltransformutil.transform.base;

/**
 * @author Minc
 * @date 2021/12/31 16:14
 */
public enum KeyWord {
    select("select", ""),
    from("from", ""),
    where("where", ""),
    group_by("group by", ""),
    order_by("order by", ""),
    like("like", ""),
    limit("limit", "");

    private final String keyword;
    private final String content;

    KeyWord(String keyword, String conetnt) {
        this.keyword = keyword;
        this.content = conetnt;
    }

    public String getKeyword() {
        return keyword;
    }

}
