package com.milesilac.citylifescan;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityScannerService {

    public static final String QUERY_FOR_CITY_NAME = "https://api.teleport.org/api/urban_areas/slug:";
    public static final String QUERY_FOR_ALL_URBAN_AREAS = "https://api.teleport.org/api/urban_areas/";
    public static final String QUERY_FOR_CITY_IMAGE = "/images/";
    public static final String QUERY_FOR_CITY_SCORES = "/scores/";
    public static final String QUERY_FOR_CITY_SALARIES = "/salaries/";
    public static final String QUERY_FOR_CITY_DETAILS = "/details/";

    Context context;

    public CityScannerService(Context context) {
        this.context = context;
    }

    public CityScannerService() {
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String test1);
    }

    public interface VolleyArrayResponseListener {
        void onError(String message);

        void onResponse(String[] names);

    }

    public interface VolleyScoreResponseListener {
        void onError(String message);

        void onResponse(String score, String summary, ArrayList<CityScore> cityScore);
    }

    public interface VolleySalaryResponseListener {
        void onError(String message);

        void onResponse(ArrayList<CitySalaries> citySalaries);
    }

    public interface VolleyDetailsResponseListener {
        void onError(String message);

        void onResponse(ArrayList<CityDetails> cityDetails);


    }





    //provide image on button click
    public void getScanResultsImage(String cityName, VolleyResponseListener volleyResponseListener) {
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
                    System.out.println("Image stack trace out");
                }

                String imgUrl = imageMobile;

                volleyResponseListener.onResponse(imgUrl);

                System.out.println("Image request out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Image not uploaded");
            }
        }); // jsonObjectRequest inner class

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        System.out.println("Image queue start");

    } //getScanResultsImage


    //provide basic info on button click
    public void getScanResultsBasicInfo(String cityName, VolleyResponseListener volleyResponseListener) {
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
                    System.out.println("Basic Info stack trace out");
                }

                String testOutput = "Full name: " + fullName + "\n" +
                                    "Continent name: " + continent + "\n" +
                                    "Current Mayor: " + currentMayor + "\n";


                volleyResponseListener.onResponse(testOutput);

                System.out.println("Info request out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        System.out.println("Info queue start");

    } //getScanResultsBasicInfo


    //provide Scores on button click
    public void getScanResultsScores(String cityName, VolleyScoreResponseListener volleyScoreResponseListener)  {
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

        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_SCORES;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                double teleport_city_score = 0;
                String summary = "";
                JSONArray categories;
                JSONObject scoresVerbose;
                ArrayList<CityScore> scores = new ArrayList<>();

                try {
                    teleport_city_score = response.getDouble("teleport_city_score");
                    summary = response.getString("summary");
                    categories = response.getJSONArray("categories");
                    for (int i=0;i<categories.length();i++) {
                        scoresVerbose = categories.getJSONObject(i);
                        scores.add(new CityScore(scoresVerbose.getString("name")
                                    ,scoresVerbose.getInt("score_out_of_10")
                                    ,scoresVerbose.getString("color")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Scores stack trace out");
                }

                String scoreResult = "Teleport City Score: " + teleport_city_score + "\n";
                String summaryResult = "Summary: " + summary;


                volleyScoreResponseListener.onResponse(scoreResult,summaryResult,scores);

                System.out.println("Scores request out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyScoreResponseListener.onError("Not a Teleport City yet");
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        System.out.println("Scores queue start");

    } //getScanResultsScores


    //provide Salaries on button click
    public void getScanResultsSalaries(String cityName, VolleySalaryResponseListener volleySalaryResponseListener) {
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

        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_SALARIES;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray salaries;
                JSONObject getSalaryDetails;
                JSONObject job;
                String title;
                JSONObject salaryPercentiles;
                double percentile_25;
                double percentile_50;
                double percentile_75;
                ArrayList<CitySalaries> salaryData = new ArrayList<>();

                try {
                    salaries = response.getJSONArray("salaries");
                    for (int i=0;i<salaries.length();i++) {
                        getSalaryDetails = salaries.getJSONObject(i);

                        job = getSalaryDetails.getJSONObject("job");
                        title = job.getString("title");

                        salaryPercentiles = getSalaryDetails.getJSONObject("salary_percentiles");
                        percentile_25 = salaryPercentiles.getDouble("percentile_25");
                        percentile_50 = salaryPercentiles.getDouble("percentile_50");
                        percentile_75 = salaryPercentiles.getDouble("percentile_75");

                        salaryData.add(new CitySalaries(title,percentile_25,percentile_50,percentile_75));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Scores stack trace out");
                }


                volleySalaryResponseListener.onResponse(salaryData);

                System.out.println("Salary data request out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        System.out.println("Scores queue start");

    } //getScanResultsSalaries


    //city name checker (autocomplete)
    public void checkCityName(VolleyArrayResponseListener volleyArrayResponseListener) {

        System.out.println("this was accessed");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_ALL_URBAN_AREAS, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                JSONObject links;
                JSONArray urbanAreas;
                JSONObject getName;
                ArrayList<String> names = new ArrayList<>();


                try {
                    links = response.getJSONObject("_links");
                    urbanAreas = links.getJSONArray("ua:item");
                    for (int i=0;i<urbanAreas.length();i++) {
                        getName = urbanAreas.getJSONObject(i);
                        names.add(getName.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("City name stack trace out");
                }


                String[] countryNames = new String[names.size()];
                for (int i=0;i<names.size();i++) {
                    countryNames[i] = names.get(i);
                }
                volleyArrayResponseListener.onResponse(countryNames);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyArrayResponseListener.onError("City Name check error");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    } //checkCityName


    //provide Details on button click
    public void getScanResultsCityDetails(String cityName, VolleyDetailsResponseListener volleyDetailsResponseListener) {
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

        String url = QUERY_FOR_CITY_NAME + cityName + QUERY_FOR_CITY_DETAILS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray categories;
                JSONObject forEachScore;
                String scoreDetailLabel;
                ArrayList<CityDetailsData> cityDetailsData = new ArrayList<>();
                ArrayList<CityDetails> cityDetails = new ArrayList<>();


                try {
                    categories = response.getJSONArray("categories");
                    for (int i=0;i<categories.length();i++) {
                        forEachScore = categories.getJSONObject(i); //get each score
                        scoreDetailLabel = forEachScore.getString("label");
                        cityDetails.add(new CityDetails(scoreDetailLabel));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Scores stack trace out");
                }



//                volleyScoreResponseListener.onResponse(scoreResult,summaryResult,scores);
                volleyDetailsResponseListener.onResponse(cityDetails);
                System.out.println("Scores request out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }); // jsonObjectRequest inner class


        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        System.out.println("Scores queue start");

    } //getScanResultsDetails


}
