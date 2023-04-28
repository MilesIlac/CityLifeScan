package com.milesilac.citylifescan.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class EntityResponse extends BaseEntityResponse {

    public int count;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
