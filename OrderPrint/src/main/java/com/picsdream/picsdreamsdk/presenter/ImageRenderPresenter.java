package com.picsdream.picsdreamsdk.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.network.ImageRenderResponse;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.ImageRenderView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Authored by vipulkumar on 01/11/17.
 */

public class ImageRenderPresenter {
    private ImageRenderView imageRenderView;

    public ImageRenderPresenter(ImageRenderView imageRenderView) {
        this.imageRenderView = imageRenderView;
    }


    public void getRenderedImage() {
        imageRenderView.onStartLoading();
        Call<ResponseBody> call = RetrofitHandler.getApiService().getInitialAppData(SharedPrefsUtil.getAppKey());
        RequestHandler.getInstance().createCall(imageRenderView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                imageRenderView.onStopLoading();
                ImageRenderResponse imageRenderResponse = (ImageRenderResponse) o;
                if (imageRenderResponse != null) {
                    imageRenderView.onImageRenderSuccess(imageRenderResponse);
                }
            }

            @Override
            public void onFailure(Error error) {
                imageRenderView.onStopLoading();
                imageRenderView.onImageRenderFailure(error);
            }
        }, ImageRenderResponse.class);
    }

    public void getRenderedImageHttp(final String imageUriString, final String type, final String medium) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("media", getFile());
        } catch (FileNotFoundException e) {
            SaneToast.getToast("Ex").show();
            e.printStackTrace();
        }
        client.post("https://www.picsdream.com/api/v1/render/" + type + "/" + medium, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                imageRenderView.onStartLoading();
            }

            @Override
            public void onFinish() {
                imageRenderView.onStopLoading();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                ImageRenderResponse imageRenderResponse = new Gson().fromJson(new String(response), ImageRenderResponse.class);
                imageRenderResponse.setType(type);
                imageRenderResponse.setImageUriString(imageUriString);
                imageRenderResponse.setMedium(medium);
                imageRenderView.onImageRenderSuccess(imageRenderResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Error error = new Error();
                error.setErrorBody(e.getMessage());
                imageRenderView.onImageRenderFailure(error);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


    public static File getFile() {
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(ContextProvider.getApplication()
                    .getContentResolver(), Uri.parse(SharedPrefsUtil.getImageUriString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "Picsdream_render" + "xx" + ".jpg";
        File cacheFile = new File(ContextProvider.getApplication().getCacheDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile.getAbsolutePath());
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("asd", "Error when saving image to cache. ", e);
            e.printStackTrace();
        }
        Uri uri = Uri.parse(ContextProvider.getApplication().getCacheDir() + "/" + filename);
        return new File(uri.getPath());
    }
}
