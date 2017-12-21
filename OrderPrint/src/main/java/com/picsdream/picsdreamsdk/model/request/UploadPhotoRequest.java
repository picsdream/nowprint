
package com.picsdream.picsdreamsdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

public class UploadPhotoRequest {

    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<File> getMedia() {
        return media;
    }

    public void setMedia(List<File> media) {
        this.media = media;
    }

    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ids")
    @Expose
    private List<String> ids;
    @SerializedName("media")
    @Expose
    private List<File> media;

}
