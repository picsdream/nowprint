package com.picsdream.picsdreamsdk.presenter;

import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.view.InitialDataView;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by vipul.kumar on 13-04-2017.
 */

public class InitialDataPresenter {
    private InitialDataView initialDataView;

    public InitialDataPresenter(InitialDataView initialDataView) {
        this.initialDataView = initialDataView;
    }

    public void getInitialAppData() {
        initialDataView.onStartLoading();
        Call<ResponseBody> call = RetrofitHandler.getApiService().getInitialAppData();
        RequestHandler.getInstance().createCall(initialDataView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                initialDataView.onStopLoading();
                InitialAppDataResponse initialAppDataResponse = (InitialAppDataResponse) o;
                if (initialAppDataResponse != null) {
                    initialDataView.onDataFetchSuccess(initialAppDataResponse);
                }
            }

            @Override
            public void onFailure(Error error) {
                initialDataView.onStopLoading();
                initialDataView.onDataFetchFailure();
            }
        }, InitialAppDataResponse.class);
    }

//    @NonNull
//    private Error getEmptyError() {
//        Error error = new Error();
//        if (initialDataView.getContext() != null) {
//            error.setErrorBody(initialDataView.getContext().getResources().getString(R.string.msg_empty_trip_list));
//        }
//        return error;
//    }
}
