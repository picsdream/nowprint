package com.picsdream.picsdreamsdk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Authored by vipulkumar on 01/11/17.
 */

public class RenderedImage {
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    private String imageUri;
    private List<Image> images;

    public static class Image {

        public Image() {

        }
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public ArrayList<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(ArrayList<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        private String medium;
        private ArrayList<String> imageUrls;
    }
}
