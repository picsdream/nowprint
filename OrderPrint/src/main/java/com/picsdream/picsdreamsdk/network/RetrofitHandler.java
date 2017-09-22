package com.picsdream.picsdreamsdk.network;


import com.picsdream.picsdreamsdk.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Instantiation of Retrofit, adding Logging Interceptor and SSO Token.
 */
public class RetrofitHandler {
    private static RetrofitHandler retrofitHandler = null;

    private RetrofitHandler() {
    }


    public static NetworkService getApiService() {
        return RetrofitHandler.getInstance().getRetrofit(Constants.BASE_URL).create(NetworkService.class);
    }

    public static NetworkService getApiService(String baseUrl) {
        return RetrofitHandler.getInstance().getRetrofit(baseUrl).create(NetworkService.class);
    }

    public static RetrofitHandler getInstance() {
        if (retrofitHandler == null) {
            synchronized (RetrofitHandler.class) {
                if (retrofitHandler == null) {
                    retrofitHandler = new RetrofitHandler();
                }
            }
        }
        return retrofitHandler;
    }

    public Retrofit getRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(0, TimeUnit.MILLISECONDS);
        builder.readTimeout(0, TimeUnit.MILLISECONDS);

//        if (Environment.showNetworkLog) {
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(Environment.showLog ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//            builder.addInterceptor(interceptor);
//        }
//
//        builder.addInterceptor(new AuthenticationInterceptor(Constants.HARDCODED_SSO));

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
