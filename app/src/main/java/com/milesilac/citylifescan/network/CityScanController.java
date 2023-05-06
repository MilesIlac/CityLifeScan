package com.milesilac.citylifescan.network;

import androidx.annotation.NonNull;

import com.milesilac.citylifescan.StringUtils;
import com.milesilac.citylifescan.model.EntityResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityScanController {

    private final CityScanService cityScanService;

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
                    } else {
                        listener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntityResponse> call, @NonNull Throwable t) {
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public void scan(String cityName, RetrofitListeners.EntityResponseListener listener, String requestType) {
        Call<EntityResponse> call = null;
        switch (requestType) {
            case "allUrbanAreas":
                call =  cityScanService.getAllUrbanAreas();
                break;
            case "imageDetails":
                call =  cityScanService.getCityImageDetails(StringUtils.formatCityName(cityName));
                break;
            case "summary":
                call =  cityScanService.getCitySummary(StringUtils.formatCityName(cityName));
                break;
            case "details":
                call =  cityScanService.getCityDetails(StringUtils.formatCityName(cityName));
                break;
            case "scores":
                call =  cityScanService.getCityScores(StringUtils.formatCityName(cityName));
                break;
            case "salaries":
                call =  cityScanService.getCitySalaries(StringUtils.formatCityName(cityName));
                break;
        }
        if (call != null) {
            enqueueCall(call, listener);
        }
    }
}
