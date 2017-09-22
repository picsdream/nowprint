package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.CountriesAdapter;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Country;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Authored by vipulkumar on 11/09/17.
 */

public class SelectCountryActivity extends BaseActivity {
    private RecyclerView rvCountries;
    private ArrayList<Country> countryList;
    private Toolbar toolbar;
    private EditText etSearch;
    private CountriesAdapter countriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        initUi();
    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        etSearch = findViewById(R.id.etSearch);
        setupToolbar(toolbar);
        toolbar.setTitle("Select your country");

        rvCountries = findViewById(R.id.rvCountries);
        countryList = new ArrayList<>();
        countryList.addAll(SharedPrefsUtil.getInitialDataResponse().getCountries());
        Utils.setRecyclerViewProperties(this, rvCountries, LinearLayoutManager.VERTICAL);
        countriesAdapter = new CountriesAdapter(this, countryList);
        rvCountries.setAdapter(countriesAdapter);

        initSearch();
    }

    private void initSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    void filter(String text){
        List<Country> temp = new ArrayList();
        for(Country country: countryList){
            if(country.getCountry().contains(text)){
                temp.add(country);
            }
        }
        countriesAdapter.updateList(temp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextProvider.trackScreenView("Select Country");
    }
}
