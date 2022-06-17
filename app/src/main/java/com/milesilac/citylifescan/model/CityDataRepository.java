package com.milesilac.citylifescan.model;

import com.milesilac.citylifescan.CityContract;
import com.milesilac.citylifescan.CityDetails;
import com.milesilac.citylifescan.CitySalaries;
import com.milesilac.citylifescan.CityScore;

import java.util.ArrayList;

public class CityDataRepository implements CityContract.CityDataRepository {

    String[] allCityNames;

    //-- imageData
    String imgUrl;
    String photographer;
    String source;
    String site;
    String license;
    String[] imageDataArray;
    //--

    //-- basicInfoData
    String basicInfo;
    //--

    //-- cityDetailsData
    ArrayList<CityDetails> cityDetails;
    //--

    //-- cityScoresData
    String teleportScore;
    String summary;
    ArrayList<CityScore> cityScores;
    //--

    //-- citySalariesData
    ArrayList<CitySalaries> citySalaries;
    //--


    @Override
    public void setAllCityNames(String[] allCityNames) { this.allCityNames = allCityNames; }

    @Override
    public String[] getAllCityNames() { return allCityNames; }

    @Override
    public void setImageData(String[] imageDataArray) { this.imageDataArray = imageDataArray; }

    @Override
    public String[] getImageData() { return imageDataArray; }

    @Override
    public String getImgUrl() { return imageDataArray[0]; }

    @Override
    public String getPhotographer() { return imageDataArray[1]; }

    @Override
    public String getSource() { return imageDataArray[2]; }

    @Override
    public String getSite() { return imageDataArray[3]; }

    @Override
    public String getLicense() { return imageDataArray[4]; }

    @Override
    public void setBasicInfo(String basicInfo) { this.basicInfo = basicInfo; }

    @Override
    public String getBasicInfo() { return basicInfo; }

    @Override
    public void setCityDetailsData(ArrayList<CityDetails> cityDetails) {
        this.cityDetails = cityDetails;
    }

    @Override
    public ArrayList<CityDetails> getCityDetailsData() { return cityDetails; }

    @Override
    public void setCityTeleportScore(String teleportScore) { this.teleportScore = teleportScore; }

    @Override
    public void setCitySummary(String summary) { this.summary = summary; }

    @Override
    public void setCityScores(ArrayList<CityScore> cityScores) { this.cityScores = cityScores; }

    @Override
    public String getTeleportScore() { return teleportScore; }

    @Override
    public String getSummary() { return summary; }

    @Override
    public ArrayList<CityScore> getCityScores() { return cityScores; }

    @Override
    public void setCitySalaries(ArrayList<CitySalaries> citySalaries) {
        this.citySalaries = citySalaries;
    }

    @Override
    public ArrayList<CitySalaries> getCitySalaries() { return citySalaries; }



}
