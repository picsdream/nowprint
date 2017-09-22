package com.picsdream.picsdreamsdk.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.picsdream.picsdreamsdk.R;
import com.picsdream.picsdreamsdk.model.Medium;
import com.picsdream.picsdreamsdk.model.SelectableItem;
import com.picsdream.picsdreamsdk.util.Constants;
import com.picsdream.picsdreamsdk.util.SharedPrefsUtil;
import com.squareup.picasso.Picasso;

/**
 * Authored by vipulkumar on 28/08/17.
 */

public class ImageFragment extends BaseFragment {
    private ImageView ivImage;
    private ViewGroup frameContainer, frameContainerBackground;

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
        setupUi(view);
    }

    private void setupUi(View view) {
        ivImage = view.findViewById(R.id.ivImage);
        frameContainer = view.findViewById(R.id.frame_container);
        frameContainerBackground = view.findViewById(R.id.frame_container_background);
        setupDummyImage();
    }

    private void setupDummyImage() {
        Uri uri = Uri.parse(SharedPrefsUtil.getImageUriString());
        Picasso.with(getContext())
                .load(uri)
                .into(ivImage);
    }

    public void onItemSelected(SelectableItem selectableItem) {
        if (selectableItem instanceof Medium) {
            clearFrames();
            if (((Medium) selectableItem).getName().equalsIgnoreCase(Constants.FRAME_METAL)) {
                makeMetalFrame();
            } else if (((Medium) selectableItem).getName().equalsIgnoreCase(Constants.FRAME_WOOD)) {
                makeWoodFrame();
            } else if (((Medium) selectableItem).getName().equalsIgnoreCase(Constants.FRAME_PAPER)) {
                makePaperFrame();
            } else if (((Medium) selectableItem).getName().equalsIgnoreCase(Constants.FRAME_CANVAS)) {
                makeCanvasFrame();
            } else {
                getActivity().getLayoutInflater().inflate(R.layout.frame_none, frameContainer);
            }
        }
    }

    private void makeWoodFrame() {
        ivImage.setAlpha(0.8f);
        getActivity().getLayoutInflater().inflate(R.layout.frame_wood, frameContainerBackground);
        changePadding(ivImage, 25);
    }

    private void makeMetalFrame() {
        getActivity().getLayoutInflater().inflate(R.layout.frame_metal, frameContainer);
        changePadding(ivImage, 0);
    }

    private void makePaperFrame() {
        getActivity().getLayoutInflater().inflate(R.layout.frame_paper, frameContainerBackground);
        changePadding(ivImage, 40);
    }

    private void makeCanvasFrame() {
        getActivity().getLayoutInflater().inflate(R.layout.frame_canvas, frameContainer);
        changePadding(ivImage, 25);
    }

    private void changePadding(final View view, final int padding) {
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//            }
//        };
//        a.setDuration(300);
//        view.startAnimation(a);
        int newPadding = padding;
        view.setPadding(newPadding, newPadding, newPadding, newPadding);
    }

    private void clearFrames() {
        ivImage.setAlpha(1f);
        frameContainerBackground.removeAllViews();
        frameContainer.removeAllViews();
    }
}
