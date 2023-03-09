package com.grocerycustomer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Flashsalelist {
    @SerializedName("flashid")
    @Expose
    private String flashid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("enddatetime")
    @Expose
    private String enddatetime;
    @SerializedName("product_data")
    @Expose
    private List<ProductItem> productData = null;

    public String getFlashid() {
        return flashid;
    }

    public void setFlashid(String flashid) {
        this.flashid = flashid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    public List<ProductItem> getProductData() {
        return productData;
    }

    public void setProductData(List<ProductItem> productData) {
        this.productData = productData;
    }

}

