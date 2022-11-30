package com.example.androidebookapp.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class qCatSpinnerList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("category_name")
    private String category_name;

    public qCatSpinnerList(String id, String category_name) {
        this.id = id;
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }
}
