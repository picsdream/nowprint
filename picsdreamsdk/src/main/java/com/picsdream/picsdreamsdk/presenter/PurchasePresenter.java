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
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.network.UploadPhotoResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.model.request.OrderItem;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.network.RequestHandler;
import com.picsdream.picsdreamsdk.network.RetrofitHandler;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.PurchaseView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Authored by vipulkumar on 03/09/17.
 */

public class PurchasePresenter {
    private PurchaseView purchaseView;

    public PurchasePresenter(PurchaseView purchaseView) {
        this.purchaseView = purchaseView;
    }

    public void purchaseItem() {
        Call<ResponseBody> call = RetrofitHandler.getApiService().purchaseItem(getPurchaseRequestHashMap());
        RequestHandler.getInstance().createCall(purchaseView, call, new RequestHandler.RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                PurchaseResponse purchaseResponse = (PurchaseResponse) o;
                if (purchaseResponse != null) {
                    purchaseView.onPurchaseSuccess();
                    SharedPrefsUtil.setTempJsonPurchase(purchaseResponse);
                    uploadPhoto(purchaseResponse);
                } else {
                    purchaseView.onPurchaseFailure();
                }
            }

            @Override
            public void onFailure(Error error) {
                purchaseView.onPurchaseFailure();
            }
        }, PurchaseResponse.class);
    }

    public void uploadPhoto(final PurchaseResponse purchaseResponse) {
        AsyncHttpClient client = new AsyncHttpClient();
        Address address = SharedPrefsUtil.getAddress();

        RequestParams params = new RequestParams();
        params.put("email", address.getEmail());
        params.put("order_no", purchaseResponse.getOrderNo());
        params.put("invoice_id", purchaseResponse.getInvoiceId());
        try {
            params.put("media[0]", getFilesArray());
        } catch (FileNotFoundException e) {
            SaneToast.getToast("Ex").show();
            e.printStackTrace();
        }
        params.put("ids[0]", purchaseResponse.getMediaIds().get1());

        client.post("https://www.picsdream.com/api/v1/purchase/upload", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(new String(response), UploadPhotoResponse.class);
                purchaseView.onUploadPhotoSuccess(uploadPhotoResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                purchaseView.onUploadPhotoFailure(purchaseResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private File[] getFilesArray() {
//        File file = new File(Uri.parse(SharedPrefsUtil.getImageUriString()).getPath());
        Bitmap bmp = null;
//        = BitmapFactory.decodeFile(Uri.parse(SharedPrefsUtil.getImageUriString()).getPath());
        try {
            bmp = MediaStore.Images.Media.getBitmap(ContextProvider.getInstance()
                    .getContentResolver(), Uri.parse(SharedPrefsUtil.getImageUriString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "Picsdream_MOM_" + "xx" + ".jpg";
        File cacheFile = new File(ContextProvider.getInstance().getCacheDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile.getAbsolutePath());
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("asd", "Error when saving image to cache. ", e);
            e.printStackTrace();
        }
        Uri uri = Uri.parse(ContextProvider.getInstance().getCacheDir() + "/" + filename);

        ArrayList<File> fileArrayList = new ArrayList<>();
        fileArrayList.add(new File(uri.getPath()));
        File[] files = new File[fileArrayList.size()];
        fileArrayList.toArray(files);
        return files;
    }


    private HashMap<String, Object> getPurchaseRequestHashMap() {
        Order order = SharedPrefsUtil.getOrder();
        Address address = SharedPrefsUtil.getAddress();

        OrderItem orderItem = new OrderItem();
        orderItem.setId("1");
        orderItem.setTotalPaid(String.valueOf(order.getTotalCost()));
        orderItem.setTotalCost(String.valueOf(order.getTotalCost()));
        orderItem.setTax(order.getTax());
        orderItem.setDiscount(order.getDiscount());
        orderItem.setFinalCost(order.getFinalCost());
        orderItem.setMedium(order.getMedium());
        orderItem.setSize(order.getSize());
        orderItem.setType(order.getType());
        orderItem.setTitle(order.getType() + " " + order.getMedium() + " " + order.getSize());

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        String addressJson = new Gson().toJson(address);
        String itemsJson = new Gson().toJson(orderItems);

        HashMap<String, Object> params = new HashMap<>();
        params.put("items", itemsJson);
        params.put("currency", order.getCurrency());
        params.put("addr", addressJson);
        params.put("mobile", address.getMobile());
        params.put("first_name", address.getName());
        params.put("total_cost", order.getTotalCost());
        params.put("discount", order.getDiscount());
        params.put("tax", order.getTax());
        params.put("total_paid", order.getFinalCost());
        params.put("title", order.getType() + " " + order.getMedium() + " " + order.getSize());

        return params;
    }
}
