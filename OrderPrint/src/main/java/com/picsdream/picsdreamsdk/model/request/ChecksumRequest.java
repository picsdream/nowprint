package com.picsdream.picsdreamsdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class ChecksumRequest {
    @SerializedName("MID")
    @Expose
    private String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getIndustryTypeID() {
        return industryTypeID;
    }

    public void setIndustryTypeID(String industryTypeID) {
        this.industryTypeID = industryTypeID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @SerializedName("ORDER_ID")
    @Expose
    private String orderId;
    @SerializedName("CUST_ID")
    @Expose
    private String custId;
    @SerializedName("INDUSTRY_TYPE_ID")
    @Expose
    private String industryTypeID;
    @SerializedName("CHANNEL_ID")
    @Expose
    private String channelID;
    @SerializedName("TXN_AMOUNT")
    @Expose
    private String txnAmount;
    @SerializedName("WEBSITE")
    @Expose
    private String website;
    @SerializedName("CALLBACK_URL")
    @Expose
    private String callbackUrl;
}
