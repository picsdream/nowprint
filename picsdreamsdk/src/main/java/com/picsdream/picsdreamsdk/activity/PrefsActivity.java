package com.picsdream.picsdreamsdk.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.fragment.ImageFragment;
import com.picsdream.picsdreamsdk.fragment.PrefFragment;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.NavigationUtil;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class PrefsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String activeFragmentTag;
    private ViewGroup proceedLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        setupUi();
        setupImageFragment();
        setupPrefFragment(Constants.TAG_TYPE);
    }

    private void setupUi() {
        findViewByIds();
        setupToolbar(toolbar);
    }

    private void setupImageFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.image_fragment_container, ImageFragment.newInstance())
                .commit();
    }

    private void setupPrefFragment(String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.prefs_fragment_container, PrefFragment.newInstance(tag), tag);
        if (!tag.equalsIgnoreCase(Constants.TAG_TYPE)) {
            transaction.addToBackStack("");
        }
        transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        transaction.commit();
    }

    private void findViewByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        proceedLayout = (ViewGroup) findViewById(R.id.proceedLayout);

        proceedLayout.setOnClickListener(this);
    }

    private void onProceedLayoutClicked() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.prefs_fragment_container);
        if (fragment != null) {
            activeFragmentTag = fragment.getTag();
            if (activeFragmentTag.equalsIgnoreCase(Constants.TAG_TYPE)) {
                setupPrefFragment(Constants.TAG_MEDIA);
            } else if (activeFragmentTag.equalsIgnoreCase(Constants.TAG_MEDIA)) {
                setupPrefFragment(Constants.TAG_SIZE);
            } else if (activeFragmentTag.equalsIgnoreCase(Constants.TAG_SIZE)) {
                NavigationUtil.startActivity(this, ReviewOrderActivity.class);
            }
        }
    }

    public void onItemSelected(SelectableItem selectableItem) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.image_fragment_container);
        if (fragment instanceof ImageFragment) {
            ((ImageFragment) fragment).onItemSelected(selectableItem);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceedLayout) {
            onProceedLayoutClicked();
        }
    }
}
