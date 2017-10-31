package com.picsdream.picsdreamsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    private ImageView ivImage;
    private ImageRenderPresenter imageRenderPresenter;
    private ProgressBar progressBar;

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
        ivImage = view.findViewById(R.id.ivImage);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setupDummyImage();
    }

    private void setupDummyImage() {
//        Uri uri = Uri.parse(SharedPrefsUtil.getImageUriString());
//        Picasso.with(getContext())
//                .load(uri)
//                .into(ivImage);
    }

    public void onItemSelected(SelectableItem selectableItem) {
        if (selectableItem instanceof Medium) {
            String storedImageUrl = getStoredImageUrl((Medium) selectableItem);
            if (storedImageUrl != null) {
                Picasso.with(getContext())
                        .load(storedImageUrl)
                        .into(ivImage);
            } else {
                imageRenderPresenter.getRenderedImageHttp(SharedPrefsUtil.getImageUriString(),
                        SharedPrefsUtil.getOrder().getType(),
                        ((Medium) selectableItem).getName());
            }
        }
    }

    private String getStoredImageUrl(Medium medium) {
        RenderedImage renderedImage = SharedPrefsUtil.getRenderedImage();
        if (renderedImage != null && renderedImage.getImageUri().equalsIgnoreCase(SharedPrefsUtil.getImageUriString())) {
            for (RenderedImage.Image image : renderedImage.getImages()) {
                if (image.getType().equalsIgnoreCase(SharedPrefsUtil.getOrder().getType()) &&
                        image.getMedium().equalsIgnoreCase(medium.getName())) {
                    return image.getImageUrl();
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
        Picasso.with(getContext())
                .load(imageRenderResponse.getImages().get(0))
                .into(ivImage);
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
        image.setImageUrl(imageRenderResponse.getImages().get(0));
        image.setMedium(imageRenderResponse.getMedium());
        images.add(image);
        renderedImage.setImages(images);
        SharedPrefsUtil.setRenderedImage(renderedImage);
    }

    @Override
    public void onStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
