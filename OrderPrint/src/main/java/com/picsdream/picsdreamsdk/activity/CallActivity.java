package com.picsdream.picsdreamsdk.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.network.DefaultResponse;
import com.picsdream.picsdreamsdk.presenter.CallPresenter;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.ValidationUtil;
import com.picsdream.picsdreamsdk.view.CallView;

public class CallActivity extends BaseActivity implements CallView {
    private EditText etMobile;
    private ViewGroup proceedLayout;
    private ProgressDialog progressDialog;
    private CallPresenter callPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        callPresenter = new CallPresenter(this);
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        etMobile = findViewById(R.id.etMobile);
        etMobile.setText(SharedPrefsUtil.getString("mobile", ""));

        proceedLayout = findViewById(R.id.proceedLayout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Requesting a Call for you...");
        proceedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidationUtil.validatePhoneNumber(etMobile)) {
                    progressDialog.show();
                    SharedPrefsUtil.setString("mobile", etMobile.getText().toString());
                    callPresenter.requestCall(etMobile.getText().toString());
                }
            }
        });
    }

    @Override
    public void onRequestCallSuccess(DefaultResponse response) {
        ContextProvider.trackEvent(APP_KEY, "Request Call Sent", etMobile.getText().toString());
        SaneToast.getToast(response.getMsg()).show();
        this.finish();
    }

    @Override
    public void onRequestCallError() {
        SaneToast.getToast("Something went wrong. Please try again").show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Request Callback");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
