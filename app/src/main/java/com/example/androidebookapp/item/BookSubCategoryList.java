package com.example.androidebookapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookSubCategoryList implements Serializable {




    @SerializedName("is_ads")
    @Expose
    private Boolean isAds;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("sub_cid")
    @Expose
    private String subCid;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("sub_category_image")
    @Expose
    private String subCategoryImage;
    @SerializedName("sub_image_thumb")
    @Expose
    private String subImageThumb;
    @SerializedName("sub_cat_status")
    @Expose
    private String subCatStatus;
    @SerializedName("native_ad_type")
    @Expose
    private String nativeAdType;
    @SerializedName("native_ad_id")
    @Expose
    private String nativeAdId;

    public Boolean getAds() {
        return isAds;
    }

    public void setAds(Boolean ads) {
        isAds = ads;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSubCid() {
        return subCid;
    }

    public void setSubCid(String subCid) {
        this.subCid = subCid;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return subCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        this.subCategoryImage = subCategoryImage;
    }

    public String getSubImageThumb() {
        return subImageThumb;
    }

    public void setSubImageThumb(String subImageThumb) {
        this.subImageThumb = subImageThumb;
    }

    public String getSubCatStatus() {
        return subCatStatus;
    }

    public void setSubCatStatus(String subCatStatus) {
        this.subCatStatus = subCatStatus;
    }

    public String getNativeAdType() {
        return nativeAdType;
    }

    public void setNativeAdType(String nativeAdType) {
        this.nativeAdType = nativeAdType;
    }

    public String getNativeAdId() {
        return nativeAdId;
    }

    public void setNativeAdId(String nativeAdId) {
        this.nativeAdId = nativeAdId;
    }
}
