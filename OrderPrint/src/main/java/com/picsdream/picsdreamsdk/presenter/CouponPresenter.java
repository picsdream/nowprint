package com.picsdream.picsdreamsdk.presenter;

import com.picsdream.picsdreamsdk.model.network.CouponsResponse;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.CouponView;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Authored by vipulkumar on 03/09/17.
 */

public class CouponPresenter {
    private CouponView couponView;

    public CouponPresenter(CouponView couponView) {
        this.couponView = couponView;
    }

    public void getAvailableCoupons() {
        Call<ResponseBody> call = RetrofitHandler.getApiService().getAvailableCoupons(SharedPrefsUtil.getAppKey());
        RequestHandler.getInstance().createCall(couponView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                CouponsResponse couponsResponse = (CouponsResponse) o;
                if (couponsResponse != null) {
                    SharedPrefsUtil.setCouponResponse(couponsResponse);
                }
            }

            @Override
            public void onFailure(Error error) {
            }
        }, CouponsResponse.class);
    }
}
