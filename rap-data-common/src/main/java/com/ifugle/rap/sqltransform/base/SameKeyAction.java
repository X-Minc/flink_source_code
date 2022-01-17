package com.ifugle.rap.sqltransform.base;

/**
 * 对相同key数据的动作
 *
 * @param <IN>
 */
public abstract class SameKeyAction<IN> {
    private KeySelector<IN> inKeySelector;

    public SameKeyAction(KeySelector<IN> inKeySelector) {
        this.inKeySelector = inKeySelector;
    }

    public abstract void sameKeyDone(IN remain, IN leave);

    public KeySelector<IN> getInKeySelector() {
        return inKeySelector;
    }

    public void setInKeySelector(KeySelector<IN> inKeySelector) {
        this.inKeySelector = inKeySelector;
    }
}