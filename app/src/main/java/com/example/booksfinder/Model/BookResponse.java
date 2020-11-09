package com.example.booksfinder.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookResponse {

    @SerializedName("items")
    @Expose
    private final List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }
}