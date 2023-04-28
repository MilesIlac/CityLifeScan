package com.milesilac.citylifescan.model;

import com.google.gson.annotations.SerializedName;

public class Entity {

    //base
    public String href;
    public String name;
    public String templated;

    //photoEntity
    public String license;
    public String photographer;
    public String site;
    public String source;

    @SerializedName("mobile")
    public String imageUrl;
}
