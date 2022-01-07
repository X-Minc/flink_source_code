package com.ifugle.rap.sqltransform.baseenum;

/**
 * @author Minc
 * @date 2022/1/4 14:25
 */
public enum Compare {

    gt(">", "gt"),
    gte(">=", "gte"),
    lt("<", "lt"),
    lte("<=", "lte"),
    in("in(", "in"),
    term("=", "term"),
    notTerm("!=", "term"),
    like("like ", "like"),
    notLike("notlike", "like"),
    isNotNull("isnotnull","isnotnull"),
    isNull("isnull","isnull");
    private final String compareOperate;
    private final String mean;

    Compare(String compareOperate, String mean) {
        this.compareOperate = compareOperate;
        this.mean = mean;
    }

    public String getCompareOperate() {
        return compareOperate;
    }

    public String getMean() {
        return mean;
    }
}
