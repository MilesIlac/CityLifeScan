package com.milesilac.citylifescan.model;

import com.google.gson.annotations.SerializedName;

public class CategoriesEntity {
    public String color;
    public String name;

    @SerializedName("score_out_of_10")
    public double scoreOutOf10;

    //details endpoint
    public Entity[] data;
    public String id;
    public String label;
}
