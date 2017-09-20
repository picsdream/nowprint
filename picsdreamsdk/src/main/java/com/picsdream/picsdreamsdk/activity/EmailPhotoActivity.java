package com.picsdream.picsdreamsdk.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.presenter.PurchasePresenter;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.ValidationUtil;

import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Authored by vipulkumar on 20/09/17.
 */

public class EmailPhotoActivity extends BaseActivity {
    private EditText etEmail;
    private ViewGroup proceedLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_photo);
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        etEmail = findViewById(R.id.etEmail);
        proceedLayout = findViewById(R.id.proceedLayout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending email...");

        proceedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidationUtil.validateEmail(etEmail)) {
                    uploadPhoto();
                }
            }
        });
    }

    public void uploadPhoto() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        Order order = SharedPrefsUtil.getOrder();

        RequestParams params = new RequestParams();
        params.put("app_id", SharedPrefsUtil.getAppKey());
        params.put("email", etEmail.getText().toString());
        params.put("medium", order.getMedium());
        params.put("type", order.getType());
        try {
            params.put("media", PurchasePresenter.getFilesArray()[0]);
        } catch (FileNotFoundException e) {
            SaneToast.getToast("Ex").show();
            e.printStackTrace();
        }

        client.post("https://www.picsdream.com/api/v1/moments/upload", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                onUploadPhotoSuccess();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                onUploadPhotoFailure();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void onUploadPhotoFailure() {
        SaneToast.getToast("Something went wrong. Please try again").show();

    }

    private void onUploadPhotoSuccess() {
        SaneToast.getToast("Photo emailed to you successfully.").show();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Send email");
    }
}
