package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.SerializedName;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class ChecksumResponse {

    @SerializedName("CHECKSUMHASH")
    private String checksumHash;

    public String getChecksumHash() {
        return checksumHash;
    }

    public void setChecksumHash(String checksumHash) {
        this.checksumHash = checksumHash;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayTmStatus() {
        return payTmStatus;
    }

    public void setPayTmStatus(int payTmStatus) {
        this.payTmStatus = payTmStatus;
    }

    @SerializedName("ORDER_ID")
    private String orderId;
    @SerializedName("payt_STATUS")
    private int payTmStatus;
}
