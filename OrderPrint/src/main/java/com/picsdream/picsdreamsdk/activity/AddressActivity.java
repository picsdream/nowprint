package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.fragment.AddressFragment;
import com.picsdream.picsdreamsdk.fragment.AddressReviewFragment;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.model.request.Address;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

/**
 * Authored by vipulkumar on 29/08/17.
 */

public class AddressActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ViewGroup proceedLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setupUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupInitialFragment();
    }

    private void setupInitialFragment() {
        Address address = SharedPrefsUtil.getAddress();
        if (address == null) {
            setupAddressFragment(false);
        } else {
            setupAddressReviewFragment();
        }
    }

    public void setupAddressReviewFragment() {
        proceedLayout.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AddressReviewFragment.newInstance())
                .commit();
    }

    public void setupAddressFragment(boolean addToBackStack) {
        proceedLayout.setVisibility(View.VISIBLE);
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AddressFragment.newInstance());
        if (addToBackStack) {
            transition.addToBackStack("");
        }
        transition.commit();
    }

    private void setupUi() {
        toolbar = findViewById(R.id.toolbar);
        proceedLayout = findViewById(R.id.proceedLayout);
        setupToolbar(toolbar);

        proceedLayout.setOnClickListener(this);

        Order order = SharedPrefsUtil.getOrder();

        ContextProvider.trackEvent(APP_KEY, "Address Form Screen", order.getType() + " - " + order.getMedium() + " - " + order.getSize());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceedLayout) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof AddressFragment) {
                if (((AddressFragment) fragment).validateUserInfo()) {
                    setupAddressReviewFragment();
                }
            } else if (fragment != null && fragment instanceof AddressReviewFragment) {
                NavigationUtil.startActivity(AddressActivity.this, PaymentActivity.class);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_notification);
        View actionView = menuItem.getActionView();
        TextView count = (TextView) actionView.findViewById(R.id.notification_pill);
        int size = Utils.notificationCount();
        count.setText(String.valueOf(size));
        if(size == 0)
            count.setVisibility(View.GONE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.onHelpItemsClicked(item, this, "Address Screen");
        return super.onOptionsItemSelected(item);
    }
}
