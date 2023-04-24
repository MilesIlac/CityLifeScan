package com.milesilac.citylifescan;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

public class CityScannerService implements VolleyListeners {

    public static final String QUERY_FOR_CITY_NAME = "https://api.teleport.org/api/urban_areas/slug:";
    public static final String QUERY_FOR_ALL_URBAN_AREAS = "https://api.teleport.org/api/urban_areas/";
    public static final String QUERY_FOR_CITY_IMAGE = "/images/";
    public static final String QUERY_FOR_CITY_SCORES = "/scores/";
    public static final String QUERY_FOR_CITY_SALARIES = "/salaries/";
    public static final String QUERY_FOR_CITY_DETAILS = "/details/";

    Context context;

    public CityScannerService(Context context) { this.context = context; }

    private String formatCityName(String cityName) {
        cityName = cityName.toLowerCase();
        if (cityName.contains(", ")) {
            cityName = cityName.replace(", ","-");
        }
        else if (cityName.contains(". ")) {
            cityName = cityName.replace(". ","-");
        }
        else if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        if (cityName.contains(".")) {
            cityName = cityName.replace(".","");
        }
        return cityName;
    }

    //city name checker (autocomplete)
    public void checkCityName(VolleyJSONResponseListener listener) {
        setRequest(QUERY_FOR_ALL_URBAN_AREAS, listener);
    }

    public void getScanResultsImage(String cityName, VolleyJSONResponseListener listener) {
        String url = QUERY_FOR_CITY_NAME + formatCityName(cityName) + QUERY_FOR_CITY_IMAGE;
        setRequest(url, listener);
    }

    public void getScanResultsBasicInfo(String cityName, VolleyJSONResponseListener listener) {
        String url = QUERY_FOR_CITY_NAME + formatCityName(cityName) + "/";
        setRequest(url, listener);
    }

    public void getScanResultsScores(String cityName, VolleyJSONResponseListener listener)  {
        String url = QUERY_FOR_CITY_NAME + formatCityName(cityName) + QUERY_FOR_CITY_SCORES;
        setRequest(url, listener);
    }

    public void getScanResultsSalaries(String cityName, VolleyJSONResponseListener listener) {
        String url = QUERY_FOR_CITY_NAME + formatCityName(cityName) + QUERY_FOR_CITY_SALARIES;
        setRequest(url, listener);
    }

    public void getScanResultsCityDetails(String cityName, VolleyJSONResponseListener listener) {
        String url = QUERY_FOR_CITY_NAME + formatCityName(cityName) + QUERY_FOR_CITY_DETAILS;
        setRequest(url, listener);
    }

    private void setRequest(String url, VolleyJSONResponseListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null , listener::onResponse, listener::onError);
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
