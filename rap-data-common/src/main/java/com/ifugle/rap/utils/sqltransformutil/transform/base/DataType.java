package com.ifugle.rap.utils.sqltransformutil.transform.base;

/**
 * @author Minc
 * @date 2021/12/31 16:38
 */
public class DataType {
    private KeyWord key;
    private String value = "";

    public DataType(KeyWord key) {
        this.key = key;
    }

    public KeyWord getKey() {
        return key;
    }

    public void setKey(KeyWord key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
