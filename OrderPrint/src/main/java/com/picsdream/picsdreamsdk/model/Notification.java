package com.picsdream.picsdreamsdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankur on 02-Dec-17.
 */

public class Notification {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("img")
    @Expose
    private String img;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("target")
    @Expose
    private String target;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("date")
    @Expose
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
