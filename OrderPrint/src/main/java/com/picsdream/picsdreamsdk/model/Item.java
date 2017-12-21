
package com.picsdream.picsdreamsdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item extends SelectableItem {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("enable_text")
    @Expose
    private Integer enable = 1;
    @SerializedName("media_heading")
    @Expose
    private String mediaHeading;
    @SerializedName("size_heading")
    @Expose
    private String sizeHeading;
    @SerializedName("size_unit")
    @Expose
    private String sizeUnit;

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    @SerializedName("img")
    @Expose
    private String imageThumb;
    @SerializedName("mediums")
    @Expose
    private List<Medium> mediums = null;
    @SerializedName("size_guide")
    @Expose
    private List<String> sizeGuideUrls = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Medium> getMediums() {
        return mediums;
    }

    public void setMediums(List<Medium> mediums) {
        this.mediums = mediums;
    }

    public String getSizeHeading() {
        return sizeHeading;
    }

    public void setSizeHeading(String sizeHeading) {
        this.sizeHeading = sizeHeading;
    }

    public String getMediaHeading() {
        return mediaHeading;
    }

    public void setMediaHeading(String mediaHeading) {
        this.mediaHeading = mediaHeading;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public List<String> getSizeGuideUrls() {
        return sizeGuideUrls;
    }

    public void setSizeGuideUrls(List<String> sizeGuideUrls) {
        this.sizeGuideUrls = sizeGuideUrls;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
