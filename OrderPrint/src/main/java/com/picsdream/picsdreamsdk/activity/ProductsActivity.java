package com.picsdream.picsdreamsdk.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.firebase.messaging.FirebaseMessaging;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.ProductsAdapter;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.services.OrderPrintFirebaseInstanceIdService;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.GridSpacingItemDecoration;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.internal.Util;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class ProductsActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rvProducts;
    InitialAppDataResponse initialAppDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("master");
        FirebaseMessaging.getInstance().subscribeToTopic(SharedPrefsUtil.getAppKey());

        initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        if(initialAppDataResponse.getItems().size() == 1) {
            setContentView(R.layout.activity_products_single);
            singleUi();
        } else {
            setContentView(R.layout.activity_products);
            setupUi();
        }
    }

    private void singleUi() {
        final Item product = initialAppDataResponse.getItems().get(0);
        RelativeLayout wrapper = findViewById(R.id.wrapper);
        ImageView ivImage = findViewById(R.id.ivImage);
        TextView tvHeading = findViewById(R.id.tvHeading);
        TextView tvDesc = findViewById(R.id.tvDesc);
        tvHeading.setText(product.getName());
        tvDesc.setText(product.getDesc());
        Picasso.with(this)
                .load(product.getImageThumb())
                .into(ivImage);
        wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = SharedPrefsUtil.getOrder();
                order.setType(product.getType());
                SharedPrefsUtil.saveOrder(order);
                ContextProvider.trackEvent(SharedPrefsUtil.getAppKey(), "Type", product.getName());
                Intent intent = new Intent(ProductsActivity.this, PrefsActivity.class);
                intent.putExtra("tag", Constants.TAG_MEDIA);
                NavigationUtil.startActivity(ProductsActivity.this, intent);
            }
        });
    }

    private void setupUi() {
        if (SharedPrefsUtil.getSandboxMode()) {
            SaneToast.getToast("Running in sandbox mode", Toast.LENGTH_LONG).show();
        }
        toolbar = findViewById(R.id.toolbar);
        rvProducts = findViewById(R.id.rvProducts);
        setupToolbar(toolbar);

        Utils.setRecyclerViewProperties(this, rvProducts, LinearLayoutManager.VERTICAL);
        List<Item> items = initialAppDataResponse.getItems();

        int spanCount = items.size() > 2 ? 2 : 1;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.addItemDecoration(new GridSpacingItemDecoration(spanCount, Utils.dpToPx(10), true));
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        rvProducts.setAdapter(new ProductsAdapter(this, items));
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
        Utils.onHelpItemsClicked(item, this, "Type Screen");
        return super.onOptionsItemSelected(item);
    }
}
