package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.CountriesAdapter;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.ArrayList;

/**
 * Authored by vipulkumar on 11/09/17.
 */

public class SelectCountryActivity extends BaseActivity {
    private RecyclerView rvCountries;
    private ArrayList<Country> countryList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        initUi();
    }

    private void initUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        toolbar.setTitle("Select a country");

        rvCountries = (RecyclerView) findViewById(R.id.rvCountries);
        countryList = new ArrayList<>();
        countryList.addAll(SharedPrefsUtil.getInitialDataResponse().getCountries());
        Utils.setRecyclerViewProperties(this, rvCountries, LinearLayoutManager.VERTICAL);
        CountriesAdapter countriesAdapter = new CountriesAdapter(this, countryList);
        rvCountries.setAdapter(countriesAdapter);
    }
}
