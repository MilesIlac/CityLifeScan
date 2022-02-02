package com.milesilac.citylifescan;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityScannerService {

    public static final String QUERY_FOR_CITY_NAME = "https://api.teleport.org/api/urban_areas/slug:";
    public static final String QUERY_FOR_CITY_IMAGE = "/images/";

    Context context;
//    String test2;

    public CityScannerService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String test1);
    }

    //provide image on button click
    public void getScanResultsImage(String cityName, VolleyResponseListener volleyResponseListener) {
        if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_IMAGE;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photos;
                JSONObject image;
                String imageMobile = "";

                try {
                    photos = response.getJSONArray("photos");
                    JSONObject getImage = photos.getJSONObject(0);
                    image = getImage.getJSONObject("image");
                    imageMobile = image.getString("mobile");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String imgUrl = imageMobile;

                volleyResponseListener.onResponse(imgUrl);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Image not uploaded");
            }
        }); // jsonObjectRequest inner class

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);



    }


    //provide all data on button click
    public void getScanResultsData(String cityName, VolleyResponseListener volleyResponseListener) {
        if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        String url = QUERY_FOR_CITY_NAME + cityName + "/";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String fullName = "";
                String continent = "";
                String currentMayor = "";


                try {
                    fullName = response.getString("full_name");
                    continent = response.getString("continent");
                    currentMayor = response.getString("mayor");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String testOutput = "Full name: " + fullName + "\n" +
                                    "Continent name: " + continent + "\n" +
                                    "Current Mayor: " + currentMayor;

                volleyResponseListener.onResponse(testOutput);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }


}
