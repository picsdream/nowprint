package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.adapter.PrefsAdapter;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.Price;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.ArrayList;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class PrefFragment extends BaseFragment {
    private RecyclerView recyclerViewMedia;
    private ArrayList<SelectableItem> items;
    private TextView tvLabel;
    private String tag;

    public static PrefFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(Constants.ITEM_FRAGMENT_TAG, tag);
        PrefFragment fragment = new PrefFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_media, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tag = getArguments().getString(Constants.ITEM_FRAGMENT_TAG);
        setupUi(view);
    }

    private void setupUi(View view) {
        items = getItemsByTag(tag);

        tvLabel = view.findViewById(R.id.tv_label);
        recyclerViewMedia = view.findViewById(R.id.recycler_view_media);
        Utils.setRecyclerViewProperties(getContext(), recyclerViewMedia, LinearLayoutManager.HORIZONTAL);
        PrefsAdapter prefsAdapter = new PrefsAdapter(getContext(), items);
        recyclerViewMedia.setAdapter(prefsAdapter);

        tvLabel.setText(getFragmentLabel(tag));
    }

    private void showRegionsDialog() {

    }

    private ArrayList<SelectableItem> getItemsByTag(String tag) {
        items = new ArrayList<>();
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Order order = SharedPrefsUtil.getOrder();
        if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            for (Item item : initialAppDataResponse.getItems()) {
                items.add(item);
            }
        } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
            for (Item item : initialAppDataResponse.getItems()) {
                if (item.getType().equalsIgnoreCase(order.getType())) {
                    for (Medium medium : item.getMediums()) {
                        items.add(medium);
                    }
                }
            }
        } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
            for (Item item : initialAppDataResponse.getItems()) {
                if (item.getType().equalsIgnoreCase(order.getType())) {
                    for (Medium medium : item.getMediums()) {
                        if (medium.getName().equalsIgnoreCase(order.getMedium()))
                        for (Price price : medium.getPrices()) {
                            items.add(price);
                        }
                    }
                }
            }
        }
        items.get(0).setSelected(true);
        return items;
    }

    private String getFragmentLabel(String tag) {
        if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            return "Select Type";
        } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
            return "Select Media";
        } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
            return "Select Size";
        }
        return "";
    }

    private void fetchItems() {

    }
}
