package com.milesilac.citylifescan.network;

import androidx.annotation.NonNull;

import com.milesilac.citylifescan.StringUtils;
import com.milesilac.citylifescan.model.EntityResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityScanController {

    private final CityScanService cityScanService;

    @Inject
    public CityScanController(CityScanService cityScanService) {
        this.cityScanService = cityScanService;
    }

    private void enqueueCall(Call<EntityResponse> call, RetrofitListeners.EntityResponseListener listener) {
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

    public void getAllUrbanAreas(RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call =  cityScanService.getAllUrbanAreas();
        enqueueCall(call, listener);
    }

    public void getCityImageDetails(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityImageDetails(StringUtils.formatCityName(cityName));
        enqueueCall(call, listener);
    }

    public void getCitySummary(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCitySummary(StringUtils.formatCityName(cityName));
        enqueueCall(call, listener);
    }

    public void getCityDetails(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityDetails(StringUtils.formatCityName(cityName));
        enqueueCall(call, listener);
    }

    public void getCityScores(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCityScores(StringUtils.formatCityName(cityName));
        enqueueCall(call, listener);
    }

    public void getCitySalaries(String cityName, RetrofitListeners.EntityResponseListener listener) {
        Call<EntityResponse> call = cityScanService.getCitySalaries(StringUtils.formatCityName(cityName));
        enqueueCall(call, listener);
    }
}
