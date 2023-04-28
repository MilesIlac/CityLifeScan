package com.milesilac.citylifescan.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.milesilac.citylifescan.StringUtils;
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

    public void getAllUrbanAreas(RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call =  cityScanService.getAllUrbanAreas();
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        EntityResponse body = response.body();
                        if (body != null) {
                            listener.onResponse(body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void getCityImageDetails(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityImageDetails(StringUtils.formatCityName(cityName));
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        EntityResponse body = response.body();
                        if (body != null) {
                            listener.onResponse(body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void getCitySummary(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCitySummary(StringUtils.formatCityName(cityName));
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        EntityResponse body = response.body();
                        if (body != null) {
                            listener.onResponse(body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void getCityDetails(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityDetails(StringUtils.formatCityName(cityName));
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        EntityResponse body = response.body();
                        if (body != null) {
                            listener.onResponse(body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void getCityScores(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityScores(StringUtils.formatCityName(cityName));
        call.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntityResponse> call, @NonNull Response<EntityResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        EntityResponse body = response.body();
                        if (body != null) {
                            listener.onResponse(body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
