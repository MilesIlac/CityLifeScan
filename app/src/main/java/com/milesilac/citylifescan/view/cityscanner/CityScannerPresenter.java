package com.milesilac.citylifescan.view.cityscanner;


import androidx.core.text.HtmlCompat;

import com.android.volley.VolleyError;
import com.milesilac.citylifescan.network.CityScannerService;
import com.milesilac.citylifescan.network.VolleyListeners;
import com.milesilac.citylifescan.model.CityDetails;
import com.milesilac.citylifescan.model.CityDetailsData;
import com.milesilac.citylifescan.model.CitySalaries;
import com.milesilac.citylifescan.model.CityScore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CityScannerPresenter implements CityScannerContract.Presenter {

    CityScannerService cityScannerService;
    CityScannerContract.View view;

    public CityScannerPresenter(CityScannerContract.View view, CityScannerService cityScannerService) {
        this.view = view;
        this.cityScannerService = cityScannerService;
    }

    @Override
    public void checkCityName() {
        cityScannerService.checkCityName(new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject links;
                JSONArray urbanAreas;
                JSONObject getName;
                ArrayList<String> names = new ArrayList<>();

                try {
                    links = jsonObject.getJSONObject("_links");
                    urbanAreas = links.getJSONArray("ua:item");
                    for (int i=0;i<urbanAreas.length();i++) {
                        getName = urbanAreas.getJSONObject(i);
                        names.add(getName.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String[] countryNames = new String[names.size()];
                for (int i=0;i<names.size();i++) {
                    countryNames[i] = names.get(i);
                }

                view.populateCityNames(countryNames);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public void getScanResults(String cityName) {

        cityScannerService.getScanResultsImage(cityName, new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray photos;
                JSONObject image;
                JSONObject attribution;
                String imageURL = "";
                String photographer = "";
                String source = "";
                String site = "";
                String license = "";

                try {
                    photos = jsonObject.getJSONArray("photos");
                    JSONObject getImage = photos.getJSONObject(0);
                    //get image
                    image = getImage.getJSONObject("image");
                    imageURL = image.getString("mobile");
                    //get attribution details
                    attribution = getImage.getJSONObject("attribution");
                    photographer = attribution.getString("photographer");
                    source = attribution.getString("source");
                    site = attribution.getString("site");
                    license =attribution.getString("license");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                view.setImageData(imageURL,photographer,source,site,license);
            }

            @Override
            public void onError(VolleyError error) {

            }

        });

        cityScannerService.getScanResultsBasicInfo(cityName, new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String fullName = "";
                String continent = "";
                String currentMayor = "";

                try {
                    fullName = jsonObject.getString("full_name");
                    continent = jsonObject.getString("continent");
                    currentMayor = jsonObject.getString("mayor");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String outputInfo = "Full name: " + fullName + "\n" +
                        "Continent name: " + continent + "\n" +
                        "Current Mayor: " + currentMayor + "\n";

                view.setBasicInfo(outputInfo);
            }

            @Override
            public void onError(VolleyError error) {

            }

        });

        cityScannerService.getScanResultsCityDetails(cityName, new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray categories;
                JSONObject forEachScore;
                String scoreDetailLabel;
                JSONArray data;
                JSONObject forEachData;
                String dataObjectName;
                String dataObjectType;
                double dataObjectDecimal;
                String dataObjectString;
                int dataObjectInt;

                ArrayList<CityDetails> cityDetails = new ArrayList<>();
                ArrayList<CityDetailsData> cityDetailsData = new ArrayList<>();

                try {
                    categories = jsonObject.getJSONArray("categories");
                    for (int i=0;i<categories.length();i++) {
                        forEachScore = categories.getJSONObject(i); //get each score
                        scoreDetailLabel = forEachScore.getString("label"); //get each score name

                        data = forEachScore.getJSONArray("data"); //get data array
                        for (int j=0;j<data.length();j++) { //scan through data array
                            forEachData = data.getJSONObject(j); //declare array element object
                            dataObjectName = forEachData.getString("label");
                            dataObjectType = forEachData.getString("type");
                            if (dataObjectType.equals("float")) {
                                dataObjectDecimal = forEachData.getDouble("float_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectDecimal)); //put each value in an array element, equivalent to one jsonobject
                            }
                            if (dataObjectType.equals("percent")) {
                                dataObjectDecimal = forEachData.getDouble("percent_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectDecimal));
                            }
                            if (dataObjectType.equals("currency_dollar")) {
                                dataObjectDecimal = forEachData.getDouble("currency_dollar_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectDecimal));
                            }
                            if (dataObjectType.equals("string")) {
                                dataObjectString = forEachData.getString("string_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectString));
                            }
                            if (dataObjectType.equals("url")) {
                                dataObjectString = forEachData.getString("url_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectString));

                            }
                            if (dataObjectType.equals("int")) {
                                dataObjectInt = forEachData.getInt("int_value");
                                cityDetailsData.add(new CityDetailsData(scoreDetailLabel,dataObjectName,dataObjectType,dataObjectInt));
                            }

                        }
                        cityDetails.add(new CityDetails(scoreDetailLabel,cityDetailsData)); //no output
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                view.setCityDetails(cityDetails, cityName);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        cityScannerService.getScanResultsSalaries(cityName, new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
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
                    salaries = jsonObject.getJSONArray("salaries");
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
                }

                view.setCitySalariesData(salaryData);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    @Override
    public void getScanResultsScores(List<CityDetails> cityDetails, String cityName) {
        cityScannerService.getScanResultsScores(cityName, new VolleyListeners.VolleyJSONResponseListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                double teleportCityScore = 0;
                String summary = "";
                JSONArray categories;
                JSONObject scoresVerbose;
                ArrayList<CityScore> scores = new ArrayList<>();

                try {
                    teleportCityScore = jsonObject.getDouble("teleport_city_score");
                    summary = jsonObject.getString("summary");
                    categories = jsonObject.getJSONArray("categories");
                    for (int i=0;i<categories.length();i++) {
                        scoresVerbose = categories.getJSONObject(i);
                        scores.add(new CityScore(scoresVerbose.getString("name")
                                ,scoresVerbose.getInt("score_out_of_10")
                                ,scoresVerbose.getString("color")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String summaryResult = "Summary: " + summary;
                String summaryString = String.valueOf(HtmlCompat.fromHtml(summaryResult,0));

                double roundedScore =  Math.round(teleportCityScore*100.0)/100.0;
                String scoreString = "Teleport City Score: " + roundedScore;

                view.setCitySummaryAndTeleportScore(summaryString, scoreString);


                ArrayList<CityScore> newCityScore = new ArrayList<>();

                for (int i=0;i<scores.size();i++) {
                    String name = scores.get(i).getName();
                    int scoreValue = scores.get(i).getScore();
                    String color = scores.get(i).getColor();
                    for (int j=0;j<cityDetails.size();j++) {
                        if (cityDetails.get(j).getCityDetailsName().equals(name)) {
                            newCityScore.add(new CityScore(name,scoreValue,color,cityDetails.get(j)));
                        }
                    }
                }

                view.setCityScores(newCityScore);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

}
