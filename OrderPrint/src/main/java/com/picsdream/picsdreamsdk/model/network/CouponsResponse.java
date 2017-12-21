package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.SerializedName;
import com.picsdream.picsdreamsdk.model.Coupon;

import java.util.List;

/**
 * Authored by vipulkumar on 29/08/17.
 */

public class CouponsResponse {
    @SerializedName("coupons")
    private List<Coupon> coupons;

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
