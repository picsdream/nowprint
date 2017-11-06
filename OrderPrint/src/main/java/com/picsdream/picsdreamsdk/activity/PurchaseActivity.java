package com.picsdream.picsdreamsdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.network.PurchaseResponse;
import com.picsdream.picsdreamsdk.model.network.UploadPhotoResponse;
import com.picsdream.picsdreamsdk.presenter.PurchasePresenter;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.picsdream.picsdreamsdk.view.PurchaseView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class PurchaseActivity extends BaseActivity implements PurchaseView, View.OnClickListener {
    private ViewGroup confirmationLayout, loadingLayout, retryLayout;
    private PurchasePresenter purchasePresenter;
    private ViewGroup proceedLayout;
    private TextView helpLayout;
    private TextView tvPrice, tvPriceText, tvOrderNo, tvdateTime;
    private boolean isCod;
    private PurchaseResponse purchaseResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        isCod = getIntent().getBooleanExtra("isCod", false);
        initUi();
        if (SharedPrefsUtil.getSandboxMode()) {
            onUploadPhotoSuccess(null);
        } else {
            purchasePresenter = new PurchasePresenter(this);
            makePurchaseCall();
        }
    }

    private void makePurchaseCall() {
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        purchasePresenter.purchaseItem();
    }

    private void makeUploadPhotoCall(PurchaseResponse purchaseResponse) {
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        purchasePresenter.uploadPhoto(purchaseResponse);
    }

    private void initUi() {
        loadingLayout = findViewById(R.id.loadingLayout);
        confirmationLayout = findViewById(R.id.confirmationLayout);
        retryLayout = findViewById(R.id.retryLayout);
        proceedLayout = findViewById(R.id.proceedLayout);
        tvPrice = findViewById(R.id.tvPrice);
        tvPriceText = findViewById(R.id.tvPriceText);
        tvOrderNo = findViewById(R.id.tvOrderNo);
        tvdateTime = findViewById(R.id.tvDateTime);
        helpLayout = findViewById(R.id.tvHelp);
        confirmationLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        helpLayout.setOnClickListener(this);
        proceedLayout.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onPurchaseFailure() {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        SaneToast.getToast("Some error occurred. Please try again").show();
        ContextProvider.trackEvent(APP_KEY, "Create order failure", "");
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextProvider.trackEvent(APP_KEY, "Create order retry", "");
                makePurchaseCall();
            }
        });
    }

    @Override
    public void onPurchaseSuccess(PurchaseResponse purchaseResponse) {
        this.purchaseResponse = purchaseResponse;
        ContextProvider.trackEvent(APP_KEY, "Create order Success", "");
    }

    @Override
    public void onUploadPhotoSuccess(UploadPhotoResponse uploadPhotoResponse) {
        loadingLayout.setVisibility(View.GONE);
        confirmationLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        ContextProvider.trackEvent(APP_KEY, "Photo Upload Success", "");
        tvPrice.setText("₹" + SharedPrefsUtil.getOrder().getFinalCost());
        if (isCod) {
            tvPriceText.setText("is payable at the time of delivery");
        } else {
            tvPriceText.setText("paid online");
        }
        if (purchaseResponse != null) {
            tvOrderNo.setText(purchaseResponse.getOrderNo());
        }
        tvdateTime.setText(getCurrentTime());
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTime().getTime());
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault());
        return fmtOut.format(date);
    }

    @Override
    public void onUploadPhotoFailure(final PurchaseResponse purchaseResponse) {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        ContextProvider.trackEvent(APP_KEY, "Photo Upload Failure", "");
        SaneToast.getToast("Some error occurred. Please try again").show();
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextProvider.trackEvent(APP_KEY, "Photo Upload Retry", "");
                makeUploadPhotoCall(purchaseResponse);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceedLayout) {
            try {
                Class<?> c = Class.forName(SharedPrefsUtil.getReturnActivityName());
                Intent intent = new Intent(PurchaseActivity.this, c);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NavigationUtil.startActivity(PurchaseActivity.this, intent);

                ContextProvider.trackEvent(APP_KEY, "Purchase complete", "Continue button clicked");

            } catch (ClassNotFoundException ignored) {
            }
        } else if (view.getId() == R.id.tvHelp) {
            Utils.intiateCall(PurchaseActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Process order");
    }
}
