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
    public static final String QUERY_FOR_CITY_SCORES = "/scores/";
    public static final String QUERY_FOR_CITY_DETAILS = "/details/";

    Context context;

    public CityScannerService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String test1);
    }

    public interface VolleyArrayResponseListener {
        void onError(String message);

        void onResponse(JSONArray jsonArray);
    }

    public interface VolleyScoreResponseListener {
        void onError(String message);

        void onResponse(String score, String summary, String scoreVerbose);
    }

    //provide image on button click
    public void getScanResultsImage(String cityName, VolleyResponseListener volleyResponseListener) {
        cityName = cityName.toLowerCase();
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

    } //getScanResultsImage


    //provide basic info on button click
    public void getScanResultsBasicInfo(String cityName, VolleyResponseListener volleyResponseListener) {
        cityName = cityName.toLowerCase();
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
                                    "Current Mayor: " + currentMayor + "\n";


                volleyResponseListener.onResponse(testOutput);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    } //getScanResultsBasicInfo

    //provide basic CoL on button click
    public void getScanResultsScores(String cityName, VolleyScoreResponseListener volleyScoreResponseListener) {
        cityName = cityName.toLowerCase();
        if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_SCORES;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                double teleport_city_score = 0;
                String summary = "";
                JSONArray categories = null;



                try {
                    teleport_city_score = response.getDouble("teleport_city_score");
                    summary = response.getString("summary");
                    categories = response.getJSONArray("categories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String scoreResult = "Teleport City Score: " + teleport_city_score + "\n";
                String summaryResult = "Summary: " + summary + "\n";
                String scoreVerboseResult = "Scores: " + categories.toString();

                volleyScoreResponseListener.onResponse(scoreResult,summaryResult,scoreVerboseResult);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyScoreResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    } //getScanResultsScores

    //provide basic CoL on button click
    public void getScanResultsCostOfLiving(String cityName, VolleyArrayResponseListener volleyArrayResponseListener) {
        cityName = cityName.toLowerCase();
        if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_DETAILS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray categories;
                JSONObject getCostOfLiving;
                JSONArray costOfLiving = null;


                try {
                    categories = response.getJSONArray("categories");
                    getCostOfLiving = categories.getJSONObject(3);
                    costOfLiving = getCostOfLiving.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                volleyArrayResponseListener.onResponse(costOfLiving);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyArrayResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    } //getScanResultsCostOfLiving


}
