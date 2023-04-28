package com.milesilac.citylifescan.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class EntityResponse extends BaseEntityResponse {

    public int count;

    //images endpoint
    public PhotosEntity[] photos;

    //basic info endpoint
    public String continent;

    @SerializedName("full_name")
    public String fullName;

    @SerializedName("is_government_partner")
    public boolean isGovernmentPartner;

    public String mayor;

    @SerializedName("name")
    public String cityName;

    public String slug;

    @SerializedName("teleport_city_url")
    public String teleportCityUrl;

    @SerializedName("ua_id")
    public String uaId;

    //for scores and details
    public CategoriesEntity[] categories;

    @SerializedName("summary")
    public String cityScoreSummary;

    @SerializedName("teleport_city_score")
    public float teleportCityScore;



    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
