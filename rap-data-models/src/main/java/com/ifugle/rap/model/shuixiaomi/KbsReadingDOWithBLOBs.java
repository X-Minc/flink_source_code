package com.ifugle.rap.model.shuixiaomi;

public class KbsReadingDOWithBLOBs extends KbsReadingDO {
    private String content;

    private String contentText;

    private String contentHtml;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText == null ? null : contentText.trim();
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml == null ? null : contentHtml.trim();
    }

    @Override
    public String toString() {
        return "KbsReadingDOWithBLOBs{" +
                "content='" + content + '\'' +
                ", contentText='" + contentText + '\'' +
                ", contentHtml='" + contentHtml + '\'' +
                '}';
    }
}
