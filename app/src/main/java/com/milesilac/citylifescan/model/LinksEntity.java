package com.milesilac.citylifescan.model;

import com.google.gson.annotations.SerializedName;

public class LinksEntity extends BaseLinksEntity {

    @SerializedName("ua:item")
    public Entity[] uaItem;

    @SerializedName("ua:scores")
    public Entity uaScores;
}
