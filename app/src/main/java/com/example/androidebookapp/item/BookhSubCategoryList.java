package com.example.androidebookapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookhSubCategoryList implements Serializable {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("ads_param")
        @Expose
        private String adsParam;
        @SerializedName("category_list")
        private List<CategoryList> categoryLists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdsParam() {
        return adsParam;
    }

    public void setAdsParam(String adsParam) {
        this.adsParam = adsParam;
    }

    public List<CategoryList> getCategoryLists() {
        return categoryLists;
    }

    public void setCategoryLists(List<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }
}
