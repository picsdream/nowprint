
package com.picsdream.picsdreamsdk.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gateways {

    @SerializedName("paytm")
    @Expose
    private Paytm paytm;
    @SerializedName("paypal")
    @Expose
    private Paypal paypal;

    public Paytm getPaytm() {
        return paytm;
    }

    public void setPaytm(Paytm paytm) {
        this.paytm = paytm;
    }

    public Paypal getPaypal() {
        return paypal;
    }

    public void setPaypal(Paypal paypal) {
        this.paypal = paypal;
    }

}
