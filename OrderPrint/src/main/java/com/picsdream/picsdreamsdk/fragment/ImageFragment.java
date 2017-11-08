package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.model.RenderedImage;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.model.network.ImageRenderResponse;
import com.picsdream.picsdreamsdk.network.Error;
import com.picsdream.picsdreamsdk.presenter.ImageRenderPresenter;
import com.picsdream.picsdreamsdk.util.SaneToast;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.picsdream.picsdreamsdk.view.ImageRenderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class ImageFragment extends BaseFragment implements ImageRenderView {
    private ImageRenderPresenter imageRenderPresenter;
    private ProgressBar progressBar;
    private static ArrayList<String> imagesToShow = new ArrayList<>();
    private ViewPager viewPager;
    private SmartTabLayout smartTabs;
    private RenderedImagePagerAdapter renderedImagePagerAdapter;

    public static ImageFragment newInstance() {
        Bundle args = new Bundle();
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageRenderPresenter = new ImageRenderPresenter(this);
        setupUi(view);
    }

    private void setupUi(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        viewPager = view.findViewById(R.id.viewPager);
        progressBar.setVisibility(View.GONE);
        setUpTabs(view);
    }

    private void setImagesToShow(ArrayList<String> storedImageUrls) {
        if (isAdded()) {
            imagesToShow = storedImageUrls;
            viewPager.setAdapter(renderedImagePagerAdapter);
            if (storedImageUrls.size() <= 1) {
                smartTabs.setVisibility(View.GONE);
            } else {
                smartTabs.setVisibility(View.VISIBLE);
                smartTabs.setViewPager(viewPager);
            }
            renderedImagePagerAdapter.notifyDataSetChanged();
        }
    }

    public void onItemSelected(SelectableItem selectableItem) {
        if (selectableItem instanceof Medium) {
            ArrayList<String> storedImageUrls = getStoredImageUrl((Medium) selectableItem);
            if (storedImageUrls != null) {
                setImagesToShow(storedImageUrls);
            } else {
                imageRenderPresenter.getRenderedImageHttp(SharedPrefsUtil.getImageUriString(),
                        SharedPrefsUtil.getOrder().getType(),
                        ((Medium) selectableItem).getName());
            }
        }
    }

    private ArrayList<String> getStoredImageUrl(Medium medium) {
        RenderedImage renderedImage = SharedPrefsUtil.getRenderedImage();
        if (renderedImage != null && renderedImage.getImageUri().equalsIgnoreCase(SharedPrefsUtil.getImageUriString())) {
            for (RenderedImage.Image image : renderedImage.getImages()) {
                if (image.getType().equalsIgnoreCase(SharedPrefsUtil.getOrder().getType()) &&
                        image.getMedium().equalsIgnoreCase(medium.getName())) {
                    return image.getImageUrls();
                }
            }
        }
        return null;
    }

    @Override
    public void onImageRenderFailure(Error error) {
        SaneToast.getToast(error.getErrorBody()).show();
    }

    @Override
    public void onImageRenderSuccess(ImageRenderResponse imageRenderResponse) {
        setImagesToShow((ArrayList<String>) imageRenderResponse.getImages());
        saveRenderedImage(imageRenderResponse);
    }

    private void saveRenderedImage(ImageRenderResponse imageRenderResponse) {
        RenderedImage renderedImage = SharedPrefsUtil.getRenderedImage();
        List<RenderedImage.Image> images;
        if (renderedImage == null || !imageRenderResponse.getImageUriString()
                .equalsIgnoreCase(renderedImage.getImageUri())) {
            renderedImage = new RenderedImage();
            images = new ArrayList<>();
            renderedImage.setImageUri(imageRenderResponse.getImageUriString());
        } else {
            images = renderedImage.getImages();
        }
        RenderedImage.Image image = new RenderedImage.Image();
        image.setType(imageRenderResponse.getType());
        image.setImageUrls((ArrayList<String>) imageRenderResponse.getImages());
        image.setMedium(imageRenderResponse.getMedium());
        images.add(image);
        renderedImage.setImages(images);
        SharedPrefsUtil.setRenderedImage(renderedImage);
    }

    @Override
    public void onStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setAdapter(null);
    }

    @Override
    public void onStopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void setUpTabs(View view) {
        renderedImagePagerAdapter = new RenderedImagePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(renderedImagePagerAdapter);

        populateScreen(view);
        viewPager.setOffscreenPageLimit(4);
    }

    private void populateScreen(View view) {
        ViewGroup tab = view.findViewById(R.id.tab_indicator);
        tab.addView(LayoutInflater.from(getContext()).inflate(R.layout.tab_layout_indicator, tab, false));

        smartTabs = view.findViewById(R.id.smart_tab_layout);
        smartTabs.setCustomTabView(new SmartTabLayout.TabProvider() {

            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                return LayoutInflater.from(getContext()).inflate(R.layout.tab_item_indicator, container, false);
            }
        });
    }


    public class RenderedImagePagerAdapter extends FragmentStatePagerAdapter {
        public RenderedImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return imagesToShow.size();
        }

        @Override
        public Fragment getItem(int position) {
            return RenderedImageFragment.newInstance(position);
        }
    }


    public static class RenderedImageFragment extends BaseFragment {
        private ImageView ivImage;
        private int position;

        public static RenderedImageFragment newInstance(int position) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            RenderedImageFragment fragment = new RenderedImageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_rendered_image, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            position = getArguments().getInt("position");
            ivImage = view.findViewById(R.id.ivImage);
            Picasso.with(getActivity())
                    .load(imagesToShow.get(position))
                    .into(ivImage);
        }
    }
}
