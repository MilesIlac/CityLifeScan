package com.milesilac.citylifescan.network;


import com.milesilac.citylifescan.model.EntityResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CityScanService {

    @GET("api/urban_areas/")
    Call<EntityResponse> getAllUrbanAreas();
}
