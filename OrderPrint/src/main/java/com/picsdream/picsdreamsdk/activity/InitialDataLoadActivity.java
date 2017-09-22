package com.picsdream.picsdreamsdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.presenter.CouponPresenter;
import com.picsdream.picsdreamsdk.presenter.InitialDataPresenter;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.picsdream.picsdreamsdk.view.CouponView;
import com.picsdream.picsdreamsdk.view.InitialDataView;

/**
 * Authored by vipulkumar on 12/09/17.
 */

public class InitialDataLoadActivity extends BaseActivity implements InitialDataView, CouponView {
    private Toolbar toolbar;
    private ViewGroup loadingLayout;
    private InitialDataPresenter initialDataPresenter;
    private CouponPresenter couponPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_data_load);

        initialDataPresenter = new InitialDataPresenter(this);
        couponPresenter = new CouponPresenter(this);
        couponPresenter.getAvailableCoupons();

        loadingLayout = (ViewGroup) findViewById(R.id.loading_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        initialDataPresenter.getInitialAppData();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onStartLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDataFetchSuccess(InitialAppDataResponse initialAppDataResponse) {
        SharedPrefsUtil.setInitialDataResponse(initialAppDataResponse);
        if (initialAppDataResponse.getLive() == 0) {
            SharedPrefsUtil.setSandboxMode(true);
        }
        Order order = new Order();
        order.setId(Utils.generateOrderId());
        SharedPrefsUtil.saveOrder(order);
        checkActivityRoute();
    }

    private void checkActivityRoute() {
        if (SharedPrefsUtil.getCountry() == null) {
            Intent intent = new Intent(this, SelectCountryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            NavigationUtil.startActivity(this, intent);
        } else {
            Intent intent = new Intent(this, PrefsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            NavigationUtil.startActivity(this, intent);
        }
    }

    @Override
    public void onDataFetchFailure(Error error) {
        finish();
        SaneToast.getToast(error.getErrorBody()).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Start Screen");
    }
}
