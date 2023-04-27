package com.milesilac.citylifescan.network;

import retrofit2.Retrofit;

public class CityScanController {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.teleport.org/")
            .build();

    CityScanService cityScanService = retrofit.create(CityScanService.class);
}
