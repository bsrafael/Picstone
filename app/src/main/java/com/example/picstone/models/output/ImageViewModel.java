package com.example.picstone.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageViewModel {
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("size")
    @Expose
    private long size;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
