package com.example.hankzz.purenote;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Note extends LitePalSupport{
    private String title;
    private String content;
    private String time;
    private int importance;
    public Note(){

    }
    public Note(String title,String content,String time){
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}
