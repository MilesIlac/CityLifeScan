package com.milesilac.citylifescan;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityScannerService {

    public static final String QUERY_FOR_CITY_NAME = "https://api.teleport.org/api/cities/?search=";

    Context context;
    String test2;

    public CityScannerService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String test2);
    }

    public void getScanResults(String cityName, VolleyResponseListener volleyResponseListener) {
        if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","%20");
        }
        String url = QUERY_FOR_CITY_NAME + cityName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                test2 = "";
                JSONObject test3;
                JSONArray test4;

                try {
                    JSONObject test1 = response.getJSONObject("_embedded");
                    test4 = test1.getJSONArray("city:search-results");
                    test3 = test4.getJSONObject(0);
                    test2 = test3.getString("matching_full_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(context, "Matched Full Name: " + test2, Toast.LENGTH_LONG).show();
                volleyResponseListener.onResponse(test2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                volleyResponseListener.onError("Still error");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
}
