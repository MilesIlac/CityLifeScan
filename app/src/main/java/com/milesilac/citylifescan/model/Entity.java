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

    //details Entity
    @SerializedName("float_value")
    public double detailFloatValue;

    @SerializedName("percent_value")
    public double detailPercentValue;

    @SerializedName("currency_dollar_value")
    public double detailCurrencyDollarValue;

    @SerializedName("string_value")
    public String detailStringValue;

    @SerializedName("url_value")
    public String detailUrlValue;

    @SerializedName("int_value")
    public double detailIntValue;

    public String id;
    public String label;
    public String type;

    @SerializedName("mobile")
    public String imageUrl;
}
