package com.picsdream.picsdreamsdk.presenter;

import com.picsdream.picsdreamsdk.model.network.DefaultResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.view.AddressView;
import com.picsdream.picsdreamsdk.view.CallView;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Ankur on 30-Nov-17.
 */

public class CallPresenter {
    private CallView callView;

    public CallPresenter (CallView callView) {
        this.callView = callView;
    }

    public void requestCall(String mobile) {
        Call<ResponseBody> call = RetrofitHandler.getApiService().requestCall(mobile);
        RequestHandler.getInstance().createCall(callView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                callView.onRequestCallSuccess((DefaultResponse) o);
            }

            @Override
            public void onFailure(Error error) {

            }
        }, DefaultResponse.class);
    }
}
