package com.milesilac.citylifescan.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.milesilac.citylifescan.model.EntityResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CityScanController {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setLenient()
            .setPrettyPrinting()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.teleport.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    CityScanService cityScanService = retrofit.create(CityScanService.class);

    public void getAllUrbanAreas(RetrofitListeners.AllUrbanAreasResponseListener listener) {
        Call<EntityResponse> call =  cityScanService.getAllUrbanAreas();
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.body() != null) {

                        listener.onResponse(response.body());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
