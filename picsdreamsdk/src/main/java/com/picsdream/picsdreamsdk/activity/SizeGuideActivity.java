package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.fragment.BaseFragment;
import com.picsdream.picsdreamsdk.model.network.InitialAppDataResponse;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.squareup.picasso.Picasso;

/**
 * Authored by vipulkumar on 20/09/17.
 */

public class SizeGuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private SmartTabLayout smartTabs;
    private InitialAppDataResponse initialAppDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_guide);
        initialAppDataResponse = SharedPrefsUtil.getInitialDataResponse();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupToolbar(toolbar);
        setUpTabs();
    }

    private void setUpTabs() {
        viewPager = findViewById(R.id.viewPager);

        SizeGuidePagerAdapter sizeGuidePagerAdapter = new SizeGuidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sizeGuidePagerAdapter);

        populateScreen();
        smartTabs.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
    }

    private void populateScreen() {
        ViewGroup tab = findViewById(R.id.tab_indicator);
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_layout_indicator, tab, false));

        smartTabs = findViewById(R.id.smart_tab_layout);
        smartTabs.setCustomTabView(new SmartTabLayout.TabProvider() {

            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View itemView = getLayoutInflater().inflate(R.layout.tab_item_indicator, container, false);
                return itemView;
            }
        });
    }

    public class SizeGuidePagerAdapter extends FragmentStatePagerAdapter {
        public SizeGuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return initialAppDataResponse.getSizeGuideSizes().size();
        }

        @Override
        public Fragment getItem(int position) {
            return SizeGuideFragment.newInstance(position);
        }
    }

    public static class SizeGuideFragment extends BaseFragment {
        private ImageView ivImage;
        private int position;

        public static SizeGuideFragment newInstance(int position) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            SizeGuideFragment fragment = new SizeGuideFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_size_guide, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            position = getArguments().getInt("position");
            InitialAppDataResponse initialData = SharedPrefsUtil.getInitialDataResponse();
            ivImage = view.findViewById(R.id.ivImage);
            Picasso.with(getActivity())
                    .load(initialData.getSizeGuideSizes().get(position))
                    .into(ivImage);
        }
    }
}
