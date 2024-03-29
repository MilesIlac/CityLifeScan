package com.milesilac.citylifescan.network;


import com.milesilac.citylifescan.model.EntityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CityScanService {

    @GET("api/urban_areas/")
    Call<EntityResponse> getAllUrbanAreas();

    @GET("api/urban_areas/slug:{cityName}/images/")
    Call<EntityResponse> getCityImageDetails(@Path("cityName") String cityName);

    @GET("api/urban_areas/slug:{cityName}/")
    Call<EntityResponse> getCitySummary(@Path("cityName") String cityName);

    @GET("api/urban_areas/slug:{cityName}/details/")
    Call<EntityResponse> getCityDetails(@Path("cityName") String cityName);

    @GET("api/urban_areas/slug:{cityName}/scores/")
    Call<EntityResponse> getCityScores(@Path("cityName") String cityName);

    @GET("api/urban_areas/slug:{cityName}/salaries/")
    Call<EntityResponse> getCitySalaries(@Path("cityName") String cityName);
}
