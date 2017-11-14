package com.picsdream.picsdreamsdk.presenter;

import com.picsdream.picsdreamsdk.model.CodResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.CodView;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Authored by vipulkumar on 31/10/17.
 */

public class CodPresenter {
    private CodView codView;

    public CodPresenter(CodView codView) {
        this.codView = codView;
    }

    public void isCodAvailable() {
        codView.onStartLoading();
        Address address = SharedPrefsUtil.getAddress();
        final Call<ResponseBody> call = RetrofitHandler.getApiService().isCodAvailable(address.getEmail(), address.getMobile(), address.getPincode());
        RequestHandler.getInstance().createCall(codView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                codView.onStopLoading();
                CodResponse codResponse = (CodResponse) o;
                if (codResponse != null) {
                    codView.onCodSuccess(codResponse);
                }
            }

            @Override
            public void onFailure(Error error) {
                codView.onStopLoading();
                codView.onCodFailure(error);
            }
        }, CodResponse.class);
    }
}
