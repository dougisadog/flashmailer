package com.doug.component.bean;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Announce {

    private String title;
    private String content;
    private String dateline;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(dateline * 1000));
        this.dateline = sd;
    }

    @Override
    public String toString() {
        return "Announce [title=" + title + ", content=" + content
                + ", dateline=" + dateline + "]";
    }

}
