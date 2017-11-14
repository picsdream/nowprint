package com.picsdream.picsdreamsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.activity.OnMobileCoverSelector;
import com.picsdream.picsdreamsdk.adapter.SearchMobileCoverAdapter;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.ArrayList;

/**
 * Authored by vipulkumar on 11/09/17.
 */

public class SearchMobileCoverFragment extends DialogFragment {
    private RecyclerView rvMedium;
    private ArrayList<Medium> mediums;
    private EditText etSearch;
    private SearchMobileCoverAdapter adapter;
    private String type;
    private OnMobileCoverSelector onMobileCoverSelector;

    public static SearchMobileCoverFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        SearchMobileCoverFragment fragment = new SearchMobileCoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof PrefFragment) {
            onMobileCoverSelector = (OnMobileCoverSelector) getParentFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mobile_cover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString("type");
        initUi(view);
    }

    private void initUi(View view) {
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        etSearch = view.findViewById(R.id.etSearch);
        rvMedium = view.findViewById(R.id.rvMedium);
        mediums = new ArrayList<>();
        ArrayList<Item> items = (ArrayList<Item>) SharedPrefsUtil.getInitialDataResponse().getItems();
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(type)) {
                mediums.addAll(item.getMediums());
            }
        }
        Utils.setRecyclerViewProperties(getContext(), rvMedium, LinearLayoutManager.VERTICAL);
        adapter = new SearchMobileCoverAdapter(getContext(), mediums, onMobileCoverSelector, this);
        rvMedium.setAdapter(adapter);

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
        ArrayList<Medium> temp = new ArrayList<>();
        for(Medium medium: mediums){
            if(medium.getText().toLowerCase().contains(text.toLowerCase())){
                temp.add(medium);
            }
        }
        adapter.updateList(temp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        ContextProvider.trackScreenView("Search Mobile Cover");
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
