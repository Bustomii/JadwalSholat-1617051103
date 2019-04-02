package com.bustomi.ilmukomputer.appjadwalsholat;

import com.bustomi.ilmukomputer.appjadwalsholat.Model.ItemsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("items")
    public List<ItemsItem> items;
    String status_valid;

    public String getStatus_valid() {
        return status_valid;
    }

    public void setStatus_valid(String status_valid) {
        this.status_valid = status_valid;
    }

    public Items(List<ItemsItem> items) {
        this.items = items;
    }

    public List<ItemsItem> getItems() {
        return items;
    }

    public void setItems(List<ItemsItem> items) {
        this.items = items;
    }
}