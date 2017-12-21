package com.picsdream.picsdreamsdk.presenter;

import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.AddressView;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Ankur on 29-Nov-17.
 */

public class AddressPresenter {
    private AddressView addressView;

    public AddressPresenter (AddressView addressView) {
        this.addressView = addressView;
    }

    public void getAddress(String email, String mobile) {
        Call<ResponseBody> call = RetrofitHandler.getApiService().getAddress(email, mobile);
        RequestHandler.getInstance().createCall(addressView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                Address address = (Address) o;
                if (address!= null) {
                    addressView.onAddressFetchSuccess((Address) o);
                } else {
                    addressView.onAddressFetchError();
                }
            }

            @Override
            public void onFailure(Error error) {
                addressView.onAddressFetchError();
            }
        }, Address.class);
    }
}
