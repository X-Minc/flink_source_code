package com.ifugle.rap.sqltransform.base;

/**
 * 对相同key数据的动作
 *
 * @param <IN>
 */
public abstract class SameKeyAction<IN> {
    private final KeySelector<IN> inKeySelector;

    public SameKeyAction(KeySelector<IN> inKeySelector) {
        this.inKeySelector = inKeySelector;
    }

    public abstract void sameKeyAction(IN remain, IN leave) throws Exception;

    public KeySelector<IN> getInKeySelector() {
        return inKeySelector;
    }
}