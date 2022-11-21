package com.example.androidebookapp.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookChapterList implements Serializable {

    @SerializedName("chapter_id")
    @Expose
    private String chapterId;
    @SerializedName("chap_subj_id")
    @Expose
    private String chapSubjId;
    @SerializedName("chap_subj_sid")
    @Expose
    private String chapSubjSid;
    @SerializedName("chap_subj_cid")
    @Expose
    private String chapSubjCid;
    @SerializedName("chap_subj_title")
    @Expose
    private String chapSubjTitle;
    @SerializedName("chap_subj_subTitle")
    @Expose
    private String chapSubjSubTitle;
    @SerializedName("chap_lang")
    @Expose
    private String chap_lang;
    @SerializedName("chap_status")
    @Expose
    private String chapStatus;
    @SerializedName("chap_subj_image")
    @Expose
    private String chapSubjImage;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapSubjId() {
        return chapSubjId;
    }

    public void setChapSubjId(String chapSubjId) {
        this.chapSubjId = chapSubjId;
    }

    public String getChapSubjSid() {
        return chapSubjSid;
    }

    public void setChapSubjSid(String chapSubjSid) {
        this.chapSubjSid = chapSubjSid;
    }

    public String getChapSubjCid() {
        return chapSubjCid;
    }

    public void setChapSubjCid(String chapSubjCid) {
        this.chapSubjCid = chapSubjCid;
    }

    public String getChapSubjTitle() {
        return chapSubjTitle;
    }

    public void setChapSubjTitle(String chapSubjTitle) {
        this.chapSubjTitle = chapSubjTitle;
    }

    public String getChapSubjSubTitle() {
        return chapSubjSubTitle;
    }

    public void setChapSubjSubTitle(String chapSubjSubTitle) {
        this.chapSubjSubTitle = chapSubjSubTitle;
    }

    public String getChapStatus() {
        return chapStatus;
    }

    public void setChapStatus(String chapStatus) {
        this.chapStatus = chapStatus;
    }

    public String getChapSubjImage() {
        return chapSubjImage;
    }

    public void setChapSubjImage(String chapSubjImage) {
        this.chapSubjImage = chapSubjImage;
    }

    public String getChap_lang() {
        return chap_lang;
    }

    public void setChap_lang(String chap_lang) {
        this.chap_lang = chap_lang;
    }
}
