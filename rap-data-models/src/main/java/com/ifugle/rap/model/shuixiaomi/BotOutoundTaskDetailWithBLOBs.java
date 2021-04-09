package com.ifugle.rap.model.shuixiaomi;

public class BotOutoundTaskDetailWithBLOBs extends BotOutoundTaskDetail {
    private String content;

    private String callRecord;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getCallRecord() {
        return callRecord;
    }

    public void setCallRecord(String callRecord) {
        this.callRecord = callRecord == null ? null : callRecord.trim();
    }
}
