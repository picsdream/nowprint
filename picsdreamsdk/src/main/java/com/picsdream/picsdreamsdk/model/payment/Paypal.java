
package com.picsdream.picsdreamsdk.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paypal {

    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("merchant")
    @Expose
    private String merchant;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

}
