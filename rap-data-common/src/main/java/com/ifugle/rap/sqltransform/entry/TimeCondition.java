package com.ifugle.rap.sqltransform.entry;

/**
 * @author Minc
 * @date 2022/1/6 15:37
 */
public enum TimeCondition {
    day("day", 1, ""),
    days30("days30", 2, " and {time_filed}>=\"{time}\""),
    month("month", 3, "and {time_filed}>=\"{time}-01 00:00:00\"");

    private final String time;
    private final Integer tableType;
    private final String condition;

    TimeCondition(String time, Integer tableType, String condition) {
        this.time = time;
        this.tableType = tableType;
        this.condition = condition;
    }

    public String getTime() {
        return time;
    }

    public Integer getTableType() {
        return tableType;
    }

    public String getCondition() {
        return condition;
    }
}
