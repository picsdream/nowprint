
package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.picsdream.picsdreamsdk.model.MediaIds;
import com.picsdream.picsdreamsdk.model.Req;

import java.util.List;

public class PurchaseResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("media_ids")
    @Expose
    private MediaIds mediaIds;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("delivery_text")
    @Expose
    private String deliveryText;
    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("req")
    @Expose
    private Req req;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("names")
    @Expose
    private List<String> names = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public MediaIds getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(MediaIds mediaIds) {
        this.mediaIds = mediaIds;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Req getReq() {
        return req;
    }

    public void setReq(Req req) {
        this.req = req;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

}
