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
import com.picsdream.picsdreamsdk.activity.SizeGuideActivity;
import com.picsdream.picsdreamsdk.adapter.PrefsAdapter;
import com.picsdream.picsdreamsdk.application.ContextProvider;
import com.picsdream.picsdreamsdk.model.Item;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.model.Order;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.NavigationUtil;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.util.Utils;

import java.util.ArrayList;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class PrefFragment extends BaseFragment {
    private ArrayList<SelectableItem> items;
    private TextView tvLabel, tvSizeGuide;
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
        tvSizeGuide = view.findViewById(R.id.tv_size_guide);
        tvLabel = view.findViewById(R.id.tv_label);
        RecyclerView recyclerViewMedia = view.findViewById(R.id.recycler_view_media);

        initPerfs(tag);
        items = getItemsByTag(tag);

        Utils.setRecyclerViewProperties(getContext(), recyclerViewMedia, LinearLayoutManager.HORIZONTAL);
        PrefsAdapter prefsAdapter = new PrefsAdapter(getContext(), items);
        recyclerViewMedia.setAdapter(prefsAdapter);

        tvLabel.setText(getFragmentLabel(tag));
    }

    private ArrayList<SelectableItem> getItemsByTag(String tag) {
        items = new ArrayList<>();
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Order order = SharedPrefsUtil.getOrder();
        if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            items.addAll(initialAppDataResponse.getItems());
        } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
            for (Item item : initialAppDataResponse.getItems()) {
                if (item.getType().equalsIgnoreCase(order.getType())) {
                    items.addAll(item.getMediums());
                }
            }
        } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
            for (Item item : initialAppDataResponse.getItems()) {
                if (item.getType().equalsIgnoreCase(order.getType())) {
                    for (Medium medium : item.getMediums()) {
                        if (medium.getName().equalsIgnoreCase(order.getMedium()))
                            items.addAll(medium.getPrices());
                    }
                }
            }
        }
        items.get(0).setSelected(true);
        return items;
    }

    private Item getSelectedItem() {
        InitialAppDataResponse initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Order order = SharedPrefsUtil.getOrder();
        for (Item item : initialAppDataResponse.getItems()) {
            if (item.getType().equalsIgnoreCase(order.getType())) {
                return item;
            }
        }
        return null;
    }

    private String getFragmentLabel(String tag) {
        if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            return "Select Type";
        } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
            return getSelectedItem().getMediaHeading();
        } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
            if (!getSelectedItem().getSizeUnit().equalsIgnoreCase("")) {
                return getSelectedItem().getSizeHeading() + " (" + getSelectedItem().getSizeUnit() + ")";
            } else {
                return getSelectedItem().getSizeHeading();
            }
        }
        return "";
    }

    private void initPerfs(String tag) {
        if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            tvSizeGuide.setVisibility(View.GONE);
        } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
            tvSizeGuide.setVisibility(View.GONE);
        } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
            tvSizeGuide.setVisibility(View.VISIBLE);
            tvSizeGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.startActivityWithClipReveal(getContext(), SizeGuideActivity.class, tvSizeGuide);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tag != null) {
            if (tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
                ContextProvider.trackScreenView("Select Type");
            } else if (tag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
                ContextProvider.trackScreenView("Select Medium");
            } else if (tag.equalsIgnoreCase(Constants.TAG_SIZE)) {
                ContextProvider.trackScreenView("Select Size");
            }
        }
    }
}
