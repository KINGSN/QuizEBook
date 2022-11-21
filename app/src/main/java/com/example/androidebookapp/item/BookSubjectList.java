package com.example.androidebookapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookSubjectList implements Serializable {


    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("show_on_home")
    @Expose
    private String showOnHome;
    @SerializedName("status")
    @Expose
    private String status;
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
    @SerializedName("subjects_id")
    @Expose
    private String subjectsId;
    @SerializedName("subj_cid")
    @Expose
    private String subjCid;
    @SerializedName("subj_sid")
    @Expose
    private String subjSid;
    @SerializedName("subj_lang_type")
    @Expose
    private String subjLangType;
    @SerializedName("subj_name")
    @Expose
    private String subjName;
    @SerializedName("subj_title")
    @Expose
    private String subjTitle;
    @SerializedName("subj_subTitle")
    @Expose
    private String subjSubTitle;
    @SerializedName("subj_image")
    @Expose
    private String subjImage;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(String showOnHome) {
        this.showOnHome = showOnHome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSubjectsId() {
        return subjectsId;
    }

    public void setSubjectsId(String subjectsId) {
        this.subjectsId = subjectsId;
    }

    public String getSubjCid() {
        return subjCid;
    }

    public void setSubjCid(String subjCid) {
        this.subjCid = subjCid;
    }

    public String getSubjSid() {
        return subjSid;
    }

    public void setSubjSid(String subjSid) {
        this.subjSid = subjSid;
    }

    public String getSubjLangType() {
        return subjLangType;
    }

    public void setSubjLangType(String subjLangType) {
        this.subjLangType = subjLangType;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public String getSubjTitle() {
        return subjTitle;
    }

    public void setSubjTitle(String subjTitle) {
        this.subjTitle = subjTitle;
    }

    public String getSubjSubTitle() {
        return subjSubTitle;
    }

    public void setSubjSubTitle(String subjSubTitle) {
        this.subjSubTitle = subjSubTitle;
    }

    public String getSubjImage() {
        return subjImage;
    }

    public void setSubjImage(String subjImage) {
        this.subjImage = subjImage;
    }
}
