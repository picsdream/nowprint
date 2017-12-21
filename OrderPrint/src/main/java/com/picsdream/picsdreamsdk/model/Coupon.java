package com.picsdream.picsdreamsdk.model;

/**
 * Authored by vipulkumar on 10/09/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("started_at")
    @Expose
    private String startedAt;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("num_value")
    @Expose
    private Integer numValue;
    @SerializedName("desc")
    @Expose
    private String desc;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getNumValue() {
        return numValue;
    }

    public void setNumValue(Integer numValue) {
        this.numValue = numValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
