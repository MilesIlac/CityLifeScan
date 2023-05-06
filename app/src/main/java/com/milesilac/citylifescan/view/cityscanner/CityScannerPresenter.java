package com.milesilac.citylifescan.view.cityscanner;


import static com.milesilac.citylifescan.StringUtils.ALL_URBAN_AREAS;
import static com.milesilac.citylifescan.StringUtils.CITY_DETAILS;
import static com.milesilac.citylifescan.StringUtils.CITY_IMAGE_DETAILS;
import static com.milesilac.citylifescan.StringUtils.CITY_SALARIES;
import static com.milesilac.citylifescan.StringUtils.CITY_SCORES;
import static com.milesilac.citylifescan.StringUtils.CITY_SUMMARY;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;

import androidx.core.text.HtmlCompat;

import com.milesilac.citylifescan.model.CategoriesEntity;
import com.milesilac.citylifescan.model.Entity;
import com.milesilac.citylifescan.model.EntityResponse;
import com.milesilac.citylifescan.model.PhotosEntity;
import com.milesilac.citylifescan.model.SalariesEntity;
import com.milesilac.citylifescan.network.CityScanController;
import com.milesilac.citylifescan.model.CityDetails;
import com.milesilac.citylifescan.model.CityDetailsData;
import com.milesilac.citylifescan.model.CitySalaries;
import com.milesilac.citylifescan.model.CityScore;


import java.util.ArrayList;
import java.util.List;


public class CityScannerPresenter implements CityScannerContract.Presenter {

    CityScannerContract.View view;
    CityScanController controller;

    public CityScannerPresenter(CityScannerContract.View view, CityScanController controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void checkCityName() {
        controller.scan("", data -> {
            EntityResponse response = (EntityResponse) data;
            Entity[] uaItems = response._links.uaItem;

            String[] countryNames = new String[uaItems.length];
            for (int i = 0, size = uaItems.length ; i < size ; i++) {
                countryNames[i] = uaItems[i].name;
            }

            view.populateCityNames(countryNames);
        }, ALL_URBAN_AREAS);
    }

    @Override
    public void getScanResults(String cityName) {
        controller.scan(cityName, data -> {
            EntityResponse response = (EntityResponse) data;
            PhotosEntity[] photos = response.photos;

            Entity attribution = photos[0].attribution;
            String license = attribution.license;
            String photographer = attribution.photographer;
            String site = attribution.site;
            String source = attribution.source;

            String imageUrl = photos[0].image.imageUrl;

            String photographerAndSite = photographer + "@" + site;
            SpannableString string = new SpannableString(photographerAndSite);
            int index = photographerAndSite.indexOf("@");
            string.setSpan(new URLSpan(source), index+1, photographerAndSite.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            view.setImageData(imageUrl,photographer,source,site,license,string);
        }, CITY_IMAGE_DETAILS);


        controller.scan(cityName, data -> {
            EntityResponse response = (EntityResponse) data;
            String fullName = response.fullName;
            String continent = response.continent;
            String currentMayor = (response.mayor == null || response.mayor.isEmpty()) ? "No Data" : response.mayor;

            String outputInfo = "Full name: " + fullName + "\n" +
                    "Continent name: " + continent + "\n" +
                    "Current Mayor: " + currentMayor + "\n";

            view.setBasicInfo(outputInfo);
        }, CITY_SUMMARY);


        controller.scan(cityName, data -> {
            EntityResponse response = (EntityResponse) data;
            CategoriesEntity[] categories = response.categories;
            ArrayList<CityDetails> cityDetails = new ArrayList<>();
            ArrayList<CityDetailsData> cityDetailsData = new ArrayList<>();

            for (CategoriesEntity category: categories) {
                String scoreDetailLabel = category.label;
                Entity[] scores = category.data;

                for (Entity score: scores) {
                    String scoreName = score.label;
                    String scoreType = score.type;
                    String scoreValue;

                    switch (scoreType) {
                        case "float":
                            scoreValue = String.valueOf(score.detailFloatValue);
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                        case "percent":
                            scoreValue = String.valueOf(score.detailPercentValue);
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                        case "currency_dollar":
                            scoreValue = String.valueOf(score.detailCurrencyDollarValue);
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                        case "string":
                            scoreValue = score.detailStringValue;
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                        case "url":
                            scoreValue = score.detailUrlValue;
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                        case "int":
                            scoreValue = String.valueOf(score.detailIntValue);
                            cityDetailsData.add(new CityDetailsData(scoreDetailLabel,scoreName,scoreType,scoreValue));
                            break;
                    }
                }
                cityDetails.add(new CityDetails(scoreDetailLabel,cityDetailsData));
            }

            view.setCityDetails(cityDetails, cityName);
        }, CITY_DETAILS);


        controller.scan(cityName, data -> {
            EntityResponse response = (EntityResponse) data;
            SalariesEntity[] salaries = response.salaries;
            ArrayList<CitySalaries> salaryData = new ArrayList<>();

            for (SalariesEntity salary: salaries) {
                String jobTitle = salary.job.title;
                double percentile25 = salary.salaryPercentiles.percentile25;
                double percentile50 = salary.salaryPercentiles.percentile50;
                double percentile75 = salary.salaryPercentiles.percentile75;

                salaryData.add(new CitySalaries(jobTitle,percentile25,percentile50,percentile75));
            }

            view.setCitySalariesData(salaryData);
        }, CITY_SALARIES);
    }

    @Override
    public void getScanResultsScores(List<CityDetails> cityDetails, String cityName) {
        controller.scan(cityName, data -> {
            EntityResponse response = (EntityResponse) data;
            double teleportCityScore = response.teleportCityScore;
            String summary = response.cityScoreSummary;
            CategoriesEntity[] categories = response.categories;
            ArrayList<CityScore> scores = new ArrayList<>();

            for (CategoriesEntity category: categories) {
                String scoreName = category.name;
                double scoreOutOf10 = category.scoreOutOf10;
                String scoreColor = category.color;

                for (CityDetails cityDetail: cityDetails) {
                    if (cityDetail.getCityDetailsName().equals(scoreName)) {
                        scores.add(new CityScore(scoreName, (int) scoreOutOf10,scoreColor, cityDetail));
                    }
                }
            }

            view.setCityScores(scores);

            String summaryResult = "Summary: " + summary;
            String summaryString = String.valueOf(HtmlCompat.fromHtml(summaryResult,0));

            double roundedScore =  Math.round(teleportCityScore*100.0)/100.0;
            String scoreString = "Teleport City Score: " + roundedScore;

            view.setCitySummaryAndTeleportScore(summaryString, scoreString);
        }, CITY_SCORES);
    }

}
