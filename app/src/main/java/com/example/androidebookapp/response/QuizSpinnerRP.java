package com.example.androidebookapp.response;

import com.example.androidebookapp.item.CatSpinnerList;
import com.example.androidebookapp.item.qCatSpinnerList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuizSpinnerRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("category_name")
    private List<CatSpinnerList> catSpinnerLists;

    @SerializedName("subcategory_name")
    private List<qCatSpinnerList> qcatSpinnerLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<CatSpinnerList> getCatSpinnerLists() {
        return catSpinnerLists;
    }

    public List<qCatSpinnerList> getQcatSpinnerLists() {
        return qcatSpinnerLists;
    }

    public void setQcatSpinnerLists(List<qCatSpinnerList> qcatSpinnerLists) {
        this.qcatSpinnerLists = qcatSpinnerLists;
    }
}
