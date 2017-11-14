package com.picsdream.picsdreamsdk.network;

import android.support.annotation.NonNull;

import com.picsdream.picsdreamsdk.model.request.ChecksumRequest;
import com.picsdream.picsdreamsdk.util.Constants;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by vipul.kumar on 06-04-2017.
 */

public interface NetworkService {

    @NonNull
    @GET(Constants.URL_INITIAL_APP_DATA)
    Call<ResponseBody> getInitialAppData(@Query("app_id") String appId);

    @NonNull
    @GET(Constants.URL_GET_AVAILABLE_COUPONS)
    Call<ResponseBody> getAvailableCoupons(@Query("app_id") String appId);

    @NonNull
    @POST(Constants.URL_GENERATE_PAYTM_CHECKSUM)
    Call<ResponseBody> generatePaytmChecksum(@Body ChecksumRequest checksumRequest);

    @NonNull
    @POST(Constants.URL_POST_PURCHASE_ITEM)
    Call<ResponseBody> purchaseItem(@Body HashMap<String, Object> purchaseRequest);

    @NonNull
    @GET(Constants.URL_IS_COD_AVAILABLE)
    Call<ResponseBody> isCodAvailable(@Query("email") String email, @Query("mobile") String mobile, @Query("pincode") String pincode);
}
