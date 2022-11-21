package com.example.androidebookapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookitemList implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("book_subjId")
    @Expose
    private String bookSubjId;
    @SerializedName("bookchapter_id")
    @Expose
    private String bookchapterId;
    @SerializedName("aid")
    @Expose
    private String aid;
    @SerializedName("featured")
    @Expose
    private String featured;
    @SerializedName("book_title")
    @Expose
    private String bookTitle;
    @SerializedName("book_description")
    @Expose
    private String bookDescription;
    @SerializedName("book_cover_img")
    @Expose
    private String bookCoverImg;
    @SerializedName("book_bg_img")
    @Expose
    private String bookBgImg;
    @SerializedName("book_file_type")
    @Expose
    private String bookFileType;
    @SerializedName("book_file_url")
    @Expose
    private String bookFileUrl;
    @SerializedName("total_rate")
    @Expose
    private String totalRate;
    @SerializedName("rate_avg")
    @Expose
    private String rateAvg;
    @SerializedName("book_views")
    @Expose
    private String bookViews;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("is_fav")
    @Expose
    private String is_fav;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getBookSubjId() {
        return bookSubjId;
    }

    public void setBookSubjId(String bookSubjId) {
        this.bookSubjId = bookSubjId;
    }

    public String getBookchapterId() {
        return bookchapterId;
    }

    public void setBookchapterId(String bookchapterId) {
        this.bookchapterId = bookchapterId;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookCoverImg() {
        return bookCoverImg;
    }

    public void setBookCoverImg(String bookCoverImg) {
        this.bookCoverImg = bookCoverImg;
    }

    public String getBookBgImg() {
        return bookBgImg;
    }

    public void setBookBgImg(String bookBgImg) {
        this.bookBgImg = bookBgImg;
    }

    public String getBookFileType() {
        return bookFileType;
    }

    public void setBookFileType(String bookFileType) {
        this.bookFileType = bookFileType;
    }

    public String getBookFileUrl() {
        return bookFileUrl;
    }

    public void setBookFileUrl(String bookFileUrl) {
        this.bookFileUrl = bookFileUrl;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(String rateAvg) {
        this.rateAvg = rateAvg;
    }

    public String getBookViews() {
        return bookViews;
    }

    public void setBookViews(String bookViews) {
        this.bookViews = bookViews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(String is_fav) {
        this.is_fav = is_fav;
    }
}
