package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.ProductsAdapter;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class ProductsActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rvProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setupUi();
    }

    private void setupUi() {
        if (SharedPrefsUtil.getSandboxMode()) {
            SaneToast.getToast("Running in sandbox mode", Toast.LENGTH_LONG).show();
        }
        toolbar = findViewById(R.id.toolbar);
        rvProducts = findViewById(R.id.rvProducts);
        setupToolbar(toolbar);
        Utils.setRecyclerViewProperties(this, rvProducts, LinearLayoutManager.VERTICAL);
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        rvProducts.setAdapter(new ProductsAdapter(this, initialAppDataResponse.getItems()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.onHelpItemsClicked(item, this, "Pref Screen");
        return super.onOptionsItemSelected(item);
    }
}
