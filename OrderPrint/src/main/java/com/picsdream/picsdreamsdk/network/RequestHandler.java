package com.picsdream.picsdreamsdk.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.view.BaseView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vipul.kumar on 13-06-2017.
 */

public class RequestHandler<T> {

    private static RequestHandler requestHandler;

    public static RequestHandler getInstance(){
        if(requestHandler == null){
            synchronized (RetrofitHandler.class){
                if(requestHandler == null){
                    requestHandler = new RequestHandler();
                }
            }
        }
        return requestHandler;
    }

    public void createCall(@NonNull final BaseView view, @NonNull Call<ResponseBody> call, @NonNull final RequestCallback<T> callback, @NonNull final Class<T> c){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(view.getContext() != null){
                    String objectString = null;
                    try {
                        objectString = parseResponse(response);
                    } catch (Exception e) {
                        Error error = new Error();
                        error.setErrorTitle("Network Error");
                        error.setErrorBody(e.getMessage());
                        callback.onFailure(error);
                    }
                    T callResponse = new Gson().fromJson(objectString, c);
                    if(callResponse != null){
                        callback.onSuccess(callResponse);
                    }else {
                        Error error = new Error();
                        error.setErrorTitle("No Data Found");
                        error.setErrorBody("An error occurred. Please try again.");
                        callback.onFailure(error);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
              if(view.getContext() != null){
                  Error error = getNetworkError();
                  callback.onFailure(error);
              }
            }
        });
    }

    @Nullable
    protected String parseResponse(@NonNull Response<ResponseBody> response) throws IOException{
        String objectString = null;
        objectString = response.body().string();
        response.body().close();
        return objectString;
    }

    public interface RequestCallback<T>{
        void onSuccess(T t);
        void onFailure(Error error);
    }

    @NonNull
    protected Error getNetworkError() {
        final Error error = new Error();
        error.setErrorTitle("Network Error");
        error.setErrorBody("Something went wrong. Please try again.");
        error.setErrorImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
        return error;
    }
}
