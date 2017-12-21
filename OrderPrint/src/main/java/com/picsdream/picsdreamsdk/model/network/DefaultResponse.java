package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankur on 30-Nov-17.
 */

public class DefaultResponse {
    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
